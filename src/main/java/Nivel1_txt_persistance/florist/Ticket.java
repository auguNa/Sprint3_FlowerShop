package Nivel1_txt_persistance.florist;

import Nivel1_txt_persistance.product_management.Product;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {
    private List<Product> products;

    public Ticket(List<Product> products) {
        this.products = products;
    }

    public double getTotalValue() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    @Override
    public String toString() {
        return "Ticket [Products: " + products + ", Total Value: €" + getTotalValue() + "]";
    }
}
