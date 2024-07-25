package nivel3_mongodb_persistance.product_management;

public abstract class Product  {
    private static int counter = 1;
    private final int id;
    private double price;

    public Product(double price) {
        this.price = price;
        this.id = counter++;
    }

    public int getCounter() {
        return counter;
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
