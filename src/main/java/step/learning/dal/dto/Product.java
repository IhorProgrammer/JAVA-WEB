package step.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Product {
    private UUID id;
    private  String name;
    private String description;
    private double price;
    private String image;

    public Product() {}

    public static Product fromResultSet(ResultSet resultSet) {
        Product product = new Product();
        try {
            product.setId( UUID.fromString( resultSet.getString("product_id" ) ) );
            product.setDescription( resultSet.getString("product_description" ) );
            product.setName( resultSet.getString("product_name" )  );
            product.setImage( resultSet.getString("product_image" ) );
            product.setPrice( resultSet.getFloat("product_price" ) );
            return product;
        } catch(Exception ex) {
            System.err.print( "Error Product.fromResultSet: " );
            System.err.println( ex.getMessage() );
        }
        return null;
    }

    public Product(UUID id, String name, String description, double price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
