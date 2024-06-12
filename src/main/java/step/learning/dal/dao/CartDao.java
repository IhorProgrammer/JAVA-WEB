package step.learning.dal.dao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.CartItem;
import step.learning.dal.dto.Product;
import step.learning.dal.dto.User;
import step.learning.services.db.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class CartDao {
    private final DBService dbService;

    @Inject
    public CartDao(DBService dbService) {
        this.dbService = dbService;
    }


    public String getCartIdByUser ( String userId ) {
        // Шукаємо чи є у користувача відкритий кошик
        String cartId = null;
        try(Statement statement = dbService.getConnection().createStatement()) {
            String sql = String.format("SELECT cart_id FROM carts WHERE cart_user='%s' AND cart_status=0 ",userId);
            ResultSet res = statement.executeQuery(sql);
            if( res.next() ) {  // є відкритий кошик
                cartId = res.getString(1);
            }
        } catch ( SQLException ex ) {
            System.err.print("Error CartDao:getCartIdByUser: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error CartDao:getCartIdByUser: ");
            System.err.println(ex.getMessage());
        }
        return cartId;
    }

    public List<CartItem> getList(String cartId){
        // Шукаємо чи є у користувача відкритий кошик
        List<CartItem> cartItems = new ArrayList<>();
        String sql;
        try(Statement statement = dbService.getConnection().createStatement()) {
            // отримати всю інфу
            sql = String.format("" +
                    "SELECT cd.cart_dt_id cart_id, cd.product_id, cart_dt_cnt count, product_image image, product_name " +
                    "FROM carts_details cd " +
                    "JOIN Products P on cd.product_id = P.product_id " +
                    "WHERE cd.cart_id = '%s'", cartId);
            ResultSet res = statement.executeQuery(sql);
            while ( res.next() ) {
                cartItems.add( CartItem.fromResultSet(res) );
            }
            return cartItems;
        } catch ( SQLException ex ) {
            System.err.print("Error CartDao:getCartsByUser: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error CartDao:getCartsByUser: ");
            System.err.println(ex.getMessage());
        }
        return cartItems;
    }

    public void add(String userId, String productId, int cnt) {
        // Шукаємо чи є у користувача відкритий кошик
        String cartId = null ;
        String sql = String.format(
                "SELECT cart_id FROM carts WHERE cart_user='%s' AND cart_status=0 ",
                userId);
        try(Statement statement = dbService.getConnection().createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            if( res.next() ) {  // є відкритий кошик
                cartId = res.getString(1);
            }
            else {  // немає відкритого кошику
                cartId = UUID.randomUUID().toString();
                sql = String.format(
                        "INSERT INTO carts(cart_id,cart_user,cart_date,cart_status) " +
                                "VALUES('%s', '%s',CURRENT_TIMESTAMP,0)",
                        cartId, userId );
                statement.executeUpdate(sql);
            }
            // cartId - id кошику чи старого, чи нового
            // Перевіряємо чи є у кошику такий товар
            sql = String.format(
                    "SELECT cart_dt_cnt FROM carts_details WHERE cart_id = '%s' AND product_id = '%s'",
                    cartId, productId);
            res = statement.executeQuery(sql);
            if( res.next() ) { // Якщо є, то збільшуємо кількість
                cnt += res.getInt(1);   // додаємо наявну та нову кількість
                sql = String.format(
                        "UPDATE carts_details SET cart_dt_cnt = %d WHERE cart_id = '%s' AND product_id = '%s'",
                        cnt, cartId, productId);
            }
            else {  // Якщо немає, то створюємо (додаємо)
                sql = String.format(
                        "INSERT INTO carts_details(cart_dt_id,cart_id,product_id,cart_dt_cnt ) " +
                                "VALUES( UUID(), '%s', '%s', %d )",
                        cartId, productId, cnt);
            }
            statement.executeUpdate(sql);
        } catch ( SQLException ex ) {
            System.err.print("Error CartDao:add: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error CartDao:add: ");
            System.err.println(ex.getMessage());
        }
    }

}
