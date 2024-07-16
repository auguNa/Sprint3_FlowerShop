package Nivel1_txt_persistance.product_management;

public class Tree extends Product {
    private double height;

    public Tree(double price, double height) {
        super(price);
        this.height = height;
    }


    @Override
    public String getAttribute() {
        return String.valueOf(height);
    }

    @Override
    public String toString() {
        return "Tree{" +
                "id=" + getId() +
                ", price=" + getPrice() +
                ", height=" + height +
                '}';
    }
}

