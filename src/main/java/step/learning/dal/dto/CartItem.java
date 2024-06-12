package step.learning.dal.dto;

import java.sql.ResultSet;
import java.util.UUID;

public class CartItem {
    private UUID id;
    private UUID productId;
    private int count;
    private String image;
    private String productName;



    public CartItem() {}

    public CartItem(UUID id, UUID productId, int count, String image, String productName) {
        this.id = id;
        this.productId = productId;
        this.count = count;
        this.image = image;
        this.productName = productName;
    }


    public static CartItem fromResultSet( ResultSet resultSet ){
        CartItem cartItem = new CartItem();
        try {
            cartItem.setId( UUID.fromString( resultSet.getString("cart_id" ) ) );
            cartItem.setProductId( UUID.fromString( resultSet.getString("product_id" ) ) );
            cartItem.setCount( resultSet.getInt("count" )  );
            cartItem.setImage( resultSet.getString("image" ) );
            cartItem.setProductName( resultSet.getString("product_name" ) );
            return cartItem;
        } catch(Exception ex) {
            System.err.print( "Error CartItem.fromResultSet: " );
            System.err.println( ex.getMessage() );
        }
        return null;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
}
