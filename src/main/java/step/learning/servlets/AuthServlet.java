package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dao.TokenDao;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.User;
import step.learning.services.kdf.KdfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Singleton
public class AuthServlet extends HttpServlet {

    private final KdfService kdfService;
    private final UserDao userDao;
    private final TokenDao tokenDao;

    @Inject
    public AuthServlet(KdfService kdfService, UserDao userDao, TokenDao tokenDao) {
        this.kdfService = kdfService;
        this.userDao = userDao;
        this.tokenDao = tokenDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if( email == null || email.isEmpty() ) {
            sendRest( resp, "error", "Property 'email' required", null ) ;
            return ;
        }
        String password = req.getParameter("password");
        if( password == null || password.isEmpty() ) {
            sendRest( resp, "error", "Property 'password' required", null ) ;
            return ;
        }

        User user = userDao.getUserByEmail( email );
        if( user == null ) {
            sendRest( resp, "error", "Credentials rejected", null );
            return;
        }

        String dk = kdfService.derivedKey( password, user.getSalt() );
        if ( ! user.getDerivedKey().equals( dk )  ) {
            sendRest( resp, "error", "Credentials rejected", null );
            return;
        }

        // генеруємо токен доступу користувача
        String token = tokenDao.getTokenByUser( user );
        JsonObject data = null;
        if(token != null) {
            data = new JsonObject();
            data.addProperty("token", token);
        }
        sendRest( resp, "success", "Credentials confirmed", data );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if( token == null || token.isEmpty() ) {
            sendRest( resp, "error", "Property 'token' required", null ) ;
            return ;
        }
        User user = userDao.getUserByToken( token );
        if ( user == null ) {
            sendRest( resp, "error", "Token invalid or expired", null ) ;

        } else {
            JsonObject data = null;
            if(token != null) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                data = gson.toJsonTree(user).getAsJsonObject();
            }

            sendRest( resp, "success", "Authorized", data ) ;

        }

    }

    private void sendRest(HttpServletResponse responce, String status, String message, JsonObject data) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();

        meta.addProperty("service", "auth");
        meta.addProperty("status", status);
        meta.addProperty("message", message);
        meta.addProperty("time", Instant.now().getEpochSecond() );

        rest.add("meta", meta);
        rest.add("data", data);

        Gson gson = new GsonBuilder().serializeNulls().create();
        responce.getWriter().print( gson.toJson( rest ) );
    }
}
