package Nivel1_txt_persistance.factory;

import com.sun.source.tree.Tree;

public class ProductFactory {
    private ProductFactory() {
}

    public static ProductFactory getInstance() {
        if (instance == null) {
            instance = new ProductFactory();
        }
        return instance;
    }

    public Product createProduct(String type, double price, String attribute) {
        switch (type.toLowerCase()) {
            case "tree":
                return new Tree(price, Double.parseDouble(attribute));
            case "flower":
                return new Flower(price, attribute);
            case "decoration":
                Material material = Material.valueOf(attribute.toUpperCase());
                return new Decoration(price, material);
            default:
                throw new IllegalArgumentException("Invalid product type");
        }
    }
}
