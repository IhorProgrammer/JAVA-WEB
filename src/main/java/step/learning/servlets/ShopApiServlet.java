package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import com.google.inject.Inject;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.dao.CartDao;
import step.learning.dal.dao.ProductDao;
import step.learning.dal.dao.TokenDao;
import step.learning.dal.dto.Product;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;

import java.io.File;
import java.util.Map;
import java.util.UUID;


@Singleton
public class ShopApiServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final ProductDao productDao;
    private final CartDao cartDao;

    private final TokenDao tokenDao;

    @Inject
    public ShopApiServlet(FormParseService formParseService, ProductDao productDao, CartDao cartDao, TokenDao tokenDao) {
        this.formParseService = formParseService;
        this.productDao = productDao;
        this.cartDao = cartDao;
        this.tokenDao = tokenDao;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("user-id");
        String productId = req.getParameter("product-id");
        cartDao.add(userId, productId, 1);
        sendRest(resp, "success", "Cart item added", null );

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult parseResult = formParseService.parse(req);
        Map<String,String> fields = parseResult.getFields() ;
        Map<String, FileItem> files = parseResult.getFiles() ;

        // Перевірити токен та його валідність
        String token = fields.get("token");
        if( token.isEmpty() ) {
            sendRest( resp, "401", "Property 'token' not valid", null ) ;
            return ;
        }
        if( !tokenDao.tokenIsValid( token ) ) {
            sendRest( resp, "403", "Property 'token' not valid", null ) ;
            return ;
        };


        // Перевірити поля з даними
        String name = fields.get("name");
        if( name == null || name.isEmpty() ) {
            sendRest( resp, "error", "Property 'name' required", null ) ;
            return ;
        }
        String price = fields.get("price");
        if( price == null || price.isEmpty() ) {
            sendRest( resp, "error", "Property 'price' required", null ) ;
            return ;
        }
        String description = fields.get("description");
        if( description == null || description.isEmpty() ) {
            sendRest( resp, "error", "Property 'description' required", null ) ;
            return ;
        }
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(name);
        product.setPrice(Double.parseDouble(price));
        product.setDescription(description);

        FileItem image = files.get("image");
        if( image != null ) {
            // image - не обов'язкове поле, але якщо воно є, то проходить перевірку
            String path = req.getServletContext().getRealPath("/") +
                    "img" + File.separator + "products" + File.separator;
            // визначаємо тип файлу (розширення)
            int dotPosition = image.getName().lastIndexOf('.');
            if( dotPosition < 0 ) {
                sendRest( resp, "error", "Image file must have extension", null ) ;
                return ;
            }
            String ext = image.getName().substring( dotPosition );
            // формуємо нове ім'я, зберігаємо розширення
            String savedName ;
            File savedFile ;
            do {
                savedName = UUID.randomUUID() + ext ;
                savedFile =  new File( path, savedName ) ;
            } while( savedFile.exists() ) ;

            try {
                image.write( savedFile );
                product.setImage( savedName );
            }
            catch (Exception ex) {
                System.err.println( ex.getMessage() );
            }
        }

        if( productDao.add(product) ) {
            sendRest(resp, "success", "Product added", product.getId().toString() );
        }
        else {
            sendRest( resp, "error", "Internal error, look at server's logs", null ) ;
        }
    }


    private void sendRest(HttpServletResponse resp, String status, String message, Object data) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
        meta.addProperty( "service", "shop"  );
        meta.addProperty( "status",  status  );
        meta.addProperty( "message", message );
        meta.addProperty( "time",    Instant.now().getEpochSecond() );
        rest.add( "meta", meta );
        Gson gson = new GsonBuilder().serializeNulls().create();
        rest.add( "data", gson.toJsonTree( data ) );
        resp.getWriter().print( gson.toJson( rest ) );
    }
}
