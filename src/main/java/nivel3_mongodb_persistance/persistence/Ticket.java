package nivel3_mongodb_persistance.persistence;

import nivel3_mongodb_persistance.product_management.Product;

import java.util.List;

public class Ticket  {
    private List<Product> products;

    public Ticket(List<Product> products) {
        this.products = products;
    }

    public double getTotalValue() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Ticket [Products: " + products + ", Total Value: €" + getTotalValue() + "]";
    }
}