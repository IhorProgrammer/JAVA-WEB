package step.learning.models;

import step.learning.dal.dto.CartItem;
import step.learning.dal.dto.Product;

import java.util.List;

public class CartPageModel {
    private List<Product> products;
    public CartPageModel(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

}
