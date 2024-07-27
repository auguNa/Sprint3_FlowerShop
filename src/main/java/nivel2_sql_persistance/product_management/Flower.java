package nivel2_sql_persistance.product_management;

public class Flower extends Product {
    private String color;

    public Flower(double price, String color) {
        super(price);
        this.color = color;
    }

    @Override
    public String getAttribute() {
        return color;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "id=" + getId() +
                ", price=" + getPrice() +
                ", color=" + color +
                '}';
    }
}