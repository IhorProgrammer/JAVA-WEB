package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.dao.CartDao;
import step.learning.dal.dao.ProductDao;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.CartItem;
import step.learning.dal.dto.Product;
import step.learning.dal.dto.User;
import step.learning.models.CartPageModel;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class CartServlet extends HttpServlet {

    private final ProductDao productDao;
    private final CartDao cartDao;
    private final UserDao userDao;

    @Inject
    public CartServlet(ProductDao productDao, CartDao cartDao, UserDao userDao) {
        this.productDao = productDao;
        this.cartDao = cartDao;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("skip-container", false);
        req.setAttribute("model", new CartPageModel(
                productDao.getList(0, 10)
        ));
        req.setAttribute("page-body", "cart");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if( token == null || token.isEmpty() ) {
            sendRest( resp, "error", "Property 'token' required", null ) ;
            return ;
        }
        User user = userDao.getUserByToken( token );
        // получить id
        String idCart = cartDao.getCartIdByUser( user.getId().toString() );
        if( idCart == null ) {
            sendRest( resp, "204", "Open cart not found", null );
            return;
        }
        // получить список
        List<CartItem> items = cartDao.getList( idCart );
        sendRest( resp, "200", "Cart found", new CartResp( items, idCart) );
    }

    private void sendRest(HttpServletResponse resp, String status, String message, Object data) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
        meta.addProperty( "service", "cart"  );
        meta.addProperty( "status",  status  );
        meta.addProperty( "message", message );
        meta.addProperty( "time",    Instant.now().getEpochSecond() );
        rest.add( "meta", meta );
        Gson gson = new GsonBuilder().serializeNulls().create();
        rest.add( "data", gson.toJsonTree( data ) );
        resp.getWriter().print( gson.toJson( rest ) );
    }

    private class CartResp {
        public List<CartItem> cart_items;
        public String id;

        public CartResp(List<CartItem> cartItems, String id) {
            this.cart_items = cartItems;
            this.id = id;
        }
    }

}
