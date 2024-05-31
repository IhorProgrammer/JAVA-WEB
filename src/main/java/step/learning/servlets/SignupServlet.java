package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.User;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;
import step.learning.services.kdf.HashKdfService;
import step.learning.services.kdf.KdfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final HashKdfService kdfService;
    private final UserDao userDao;

    @Inject
    public SignupServlet(FormParseService formParseService, HashKdfService kdfService, UserDao userDao) {
        this.formParseService = formParseService;
        this.kdfService = kdfService;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        userDao.installTable();
        req.setAttribute("page-body", "signup");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult formParseResult = formParseService.parse( req );
//        String json = String.format(
//                "{\"fields\": %d, \"files\": %d}",
//                formParseResult.getFields().size(),
//                formParseResult.getFiles().size()
//        );
//        resp.getWriter().print( json );
        Map<String, String> fields = formParseResult.getFields();
        Map<String, FileItem> files = formParseResult.getFiles();

        String userName = fields.get("user-name");
        if( userName == null && userName.isEmpty() ) {
            sendRest( resp, "error", "Property 'user-name' required", null );
            return;
        }
        String userEmail = fields.get("user-email");
        if( userEmail == null && userEmail.isEmpty() ) {
            sendRest( resp, "error", "Property 'user-email' required", null );
            return;
        }
        String userPassword = fields.get("user-password");
        if( userPassword == null && userPassword.isEmpty() ) {
            sendRest( resp, "error", "Property 'user-password' required", null );
            return;
        }
        User user = new User();
        user.setId( UUID.randomUUID() );
        user.setName( userName );
        user.setEmail( userEmail );

        user.setSalt( kdfService.derivedKey( UUID.randomUUID().toString(), "" ) );
        user.setDerivedKey( kdfService.derivedKey( userPassword, user.getSalt() ) ) ;


        FileItem avatar = files.get("user-avatar");
        if( avatar != null ) {
            // Якщо аватар є. *Аватар не обовязкове поле
            String path = req.getServletContext()
                    .getRealPath("/")
                    + "img"
                    + File.separator
                    + "avatar"
                    + File.separator;

            int dotPosition = avatar.getName().lastIndexOf('.');
            if( dotPosition < 0 ) {
                sendRest( resp, "error", "Avatar file must be have extension", null );
                return;
            }

            String ext = avatar.getName().substring( dotPosition );
            String savedName;
            File savedFile;

            do {
                savedName = UUID.randomUUID() + ext;
                savedFile = new File( path, savedName );
            } while ( savedFile.exists() );

            try {
                avatar.write( savedFile );
                user.setAvatar( savedName );
            } catch ( Exception e ) {
                System.err.println("Error SignupServlet:doPost \n" + e.getMessage());
            }
        }


        // реєструємо користувача у БД
        if( userDao.registerUser( user ) ) {
            sendRest( resp, "success", "User registered", null );
        }
        else {
            sendRest( resp, "error", "Interal error, look at server's logs", null );
        }
    }

    private void sendRest(HttpServletResponse responce, String status, String message, String id) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();

        meta.addProperty("service", "signup");
        meta.addProperty("status", status);
        meta.addProperty("message", message);
        meta.addProperty("time", Instant.now().getEpochSecond() );

        rest.add("meta", meta);

        JsonObject data = null;
        if(id != null) {
            data = new JsonObject();
            data.addProperty("id", id);
        }
        rest.add("data", data);

        Gson gson = new GsonBuilder().serializeNulls().create();
        responce.getWriter().print( gson.toJson( rest ) );
    }
}

/*
* REST {
*   meta: {
*       service: "signup"
*       status: "success"
*       message: "User created"
*       time: 18912
*   }
*   data {
*       id: "....."
*   }
* }
*
* */
