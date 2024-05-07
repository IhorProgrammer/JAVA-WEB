package step.learning.dal.dto;
import java.util.UUID;

public class PromotionalItem {
    private UUID id;
    private String productName;
    private double promotionalPrice;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PromotionalItem(UUID id, String productName, double promotionalPrice, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.promotionalPrice = promotionalPrice;
        this.imageUrl = imageUrl;
    }

    public PromotionalItem() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPromotionalPrice() {
        return promotionalPrice;
    }

    public void setPromotionalPrice(double promotionalPrice) {
        this.promotionalPrice = promotionalPrice;
    }
}
