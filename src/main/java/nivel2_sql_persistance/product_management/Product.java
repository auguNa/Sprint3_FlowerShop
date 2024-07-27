package nivel2_sql_persistance.product_management;

<<<<<<< HEAD:src/main/java/Nivel1_txt_persistance/product_management/Product.java
import java.io.Serializable;

public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;

=======
public abstract class Product  {
>>>>>>> sql_persistance:src/main/java/nivel2_sql_persistance/product_management/Product.java
    private static int counter = 1;
    private int id;
    private double price;

    public Product(double price) {
        this.price = price;
        this.id = 0; // counter++;
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

    public void setId(int id) {
        this.id = id;
    }

    public abstract String getAttribute();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [ID=" + id + ", price=" + price + "]";
    }
}