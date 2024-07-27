package nivel2_sql_persistance.product_management;

public class Decoration extends Product {
    private Material material;

    public Decoration(double price, Material material) {
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