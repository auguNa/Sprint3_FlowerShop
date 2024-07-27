package nivel2_sql_persistance.product_management;

public class Decoration extends nivel2_sql_persistance.product_management.Product {
    private nivel2_sql_persistance.product_management.Material material;

    public Decoration(double price, nivel2_sql_persistance.product_management.Material material) {
        super(price);
        this.material = material;
    }

    @Override
    public String getAttribute() {
        return material.name();
    }

    @Override
    public String toString() {
        return "Decoration{" +
                "id=" + getId() +
                ", price=" + getPrice() +
                ", material=" + material +
                '}';
    }
}