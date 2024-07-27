package nivel2_sql_persistance.dao;


import nivel2_sql_persistance.product_management.Material;
import nivel2_sql_persistance.product_management.Decoration;
import nivel2_sql_persistance.product_management.Flower;
import nivel2_sql_persistance.product_management.Product;
import nivel2_sql_persistance.product_management.Tree;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {
    private Connection connection;

    public StockDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveNewProduct(Product product) throws SQLException {
        String insertQuery = "INSERT INTO Product (florist_id, stock, price, product_type, height, color, material) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, getFloristId()); // Assume single florist, default ID
            stmt.setBoolean(2, true); // Product is in stock
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, getProductType(product));
            stmt.setObject(5, getHeight(product));
            stmt.setString(6, getColor(product));
            stmt.setString(7, getMaterial(product));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to insert " + product.getClass().getSimpleName() + ".");
            }
        }
    }

    public List<Product> getStockFromDatabase() {
        return getProductsByStockStatus(true); // true for in stock
    }

    public void updateStockValue(int productId) throws SQLException {
        String query = "UPDATE Product SET stock = false WHERE id = ? AND stock = true";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Product with ID " + productId + " not found or already marked as out of stock.");
            }
        }
    }

    public void deleteProduct(int id) throws SQLException {
        String deleteQuery = "DELETE FROM Product WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Product with ID " + id + " not found.");
            }
        }
    }

    private List<Product> getProductsByStockStatus(boolean inStock) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE stock = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBoolean(1, inStock);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    double price = rs.getDouble("price");
                    String type = rs.getString("product_type");
                    Double height = rs.getObject("height", Double.class);
                    String color = rs.getString("color");
                    String material = rs.getString("material");

                    Product product = createProduct(type, price, height, color, material);
                    if (product != null) {
                        product.setId(id);
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products from database: " + e.getMessage());
        }
        return products;
    }

    private Product createProduct(String productType, double price, Double height, String color, String material) {
        switch (productType) {
            case "DECORATION":
                return new Decoration(price, Material.valueOf(material));
            case "FLOWER":
                return new Flower(price, color);
            case "TREE":
                return new Tree(price, height);
            default:
                return null;
        }
    }

    private int getFloristId() {
        String query = "SELECT id FROM Florist LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("No florist found.");
            }
        } catch (SQLException e) {
            System.err.println("Error getting florist ID: " + e.getMessage());
            return 1;
        }
    }

    private String getProductType(Product product) {
        if (product instanceof Tree) return "TREE";
        if (product instanceof Flower) return "FLOWER";
        if (product instanceof Decoration) return "DECORATION";
        throw new IllegalArgumentException("Unknown product type: " + product.getClass().getSimpleName());
    }

    private String getHeight(Product product) {
        if (product instanceof Tree) return (product).getAttribute();
        return null;
    }

    private String getColor(Product product) {
        if (product instanceof Flower) return (product).getAttribute();
        return null;
    }

    private String getMaterial(Product product) {
        if (product instanceof Decoration) return (product).getAttribute();
        return null;
    }
}