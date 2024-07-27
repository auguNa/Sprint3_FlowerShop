package nivel2_sql_persistance.product_management;

public class Tree extends nivel2_sql_persistance.product_management.Product {

<<<<<<< HEAD:src/main/java/Nivel1_txt_persistance/product_management/Tree.java
import java.io.Serializable;

public class Tree extends Product  {


=======
>>>>>>> sql_persistance:src/main/java/nivel2_sql_persistance/product_management/Tree.java
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