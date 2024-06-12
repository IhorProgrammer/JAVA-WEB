package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.Product;
import step.learning.dal.dto.User;
import step.learning.services.db.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class ProductDao {

    private final DBService dbService;

    @Inject
    public ProductDao(DBService dbService) {
        this.dbService = dbService;
    }

    public List<Product> getList(int skip, int take ) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products LIMIT ?, ?";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement( sql )) {
            prep.setInt(1, skip);
            prep.setInt(2, take);
            ResultSet resultSet = prep.executeQuery();
            while (resultSet.next()) {
                products.add( Product.fromResultSet(resultSet) );
            }

        } catch ( SQLException ex ) {
            System.err.print("Error UserDao:getUserByToken: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error UserDao:getUserByToken: ");
            System.err.println(ex.getMessage());
        }
        return products;


    }

    public boolean add( Product product ) {
        if( product == null ) return false ;
        if( product.getId() == null ) product.setId( UUID.randomUUID() );

        String sql = "INSERT INTO Products" +
                "(product_id,product_name,product_price,product_description,product_image ) " +
                "VALUES(?,?,?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, product.getId().toString() );   // у JDBC відлік від 1
            prep.setString( 2, product.getName() );
            prep.setDouble( 3, product.getPrice() );
            prep.setString( 4, product.getDescription() );
            prep.setString( 5, product.getImage() );
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
            return false ;
        }
    }
}
