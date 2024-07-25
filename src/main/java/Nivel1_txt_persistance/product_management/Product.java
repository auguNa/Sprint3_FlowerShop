package Nivel1_txt_persistance.product_management;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1;
    private final int id;
    private double price;

    public Product(double price) {
        this.price = price;
        this.id = counter++;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public abstract String getAttribute();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [ID=" + id + ", price=" + price + "]";
    }
}
