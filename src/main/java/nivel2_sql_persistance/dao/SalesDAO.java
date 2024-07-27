package nivel2_sql_persistance.dao;

import nivel2_sql_persistance.persistence.Ticket;
import nivel2_sql_persistance.product_management.*;

import java.sql.*;
import java.util.*;

public class SalesDAO {
    private Connection connection;

    public SalesDAO(Connection connection) {
        this.connection = connection;
    }

    public void saveSale(Ticket ticket) throws SQLException {
        String insertTicketQuery = "INSERT INTO Ticket (florist_id, sale_date) VALUES (?, ?)";
        String insertSaleQuery = "INSERT INTO Sale (ticket_id, product_id) VALUES (?, ?)";

        try (PreparedStatement insertTicketStmt = connection.prepareStatement(insertTicketQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertTicketStmt.setInt(1, getFloristId());
            insertTicketStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            int affectedRows = insertTicketStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ticket failed, no rows affected.");
            }

            try (ResultSet generatedKeys = insertTicketStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int ticketId = generatedKeys.getInt(1);
                    saveProductsForTicket(ticketId, ticket.getProducts(), insertSaleQuery);
                } else {
                    throw new SQLException("Creating ticket failed, no ID obtained.");
                }
            }
        }
    }

    private void saveProductsForTicket(int ticketId, List<Product> products, String insertSaleQuery) throws SQLException {
        try (PreparedStatement insertSaleStmt = connection.prepareStatement(insertSaleQuery)) {
            for (Product product : products) {
                insertSaleStmt.setInt(1, ticketId);
                insertSaleStmt.setInt(2, product.getId());
                insertSaleStmt.addBatch();
            }
            insertSaleStmt.executeBatch();
        }
    }

    public Map<Integer, Ticket> getTicketsFromDatabase() {
        Map<Integer, Ticket> ticketsMap = new HashMap<>();
        String ticketQuery = "SELECT * FROM Ticket";
        String saleQuery = "SELECT product_id FROM Sale WHERE ticket_id = ?";

        try (PreparedStatement ticketStmt = connection.prepareStatement(ticketQuery);
             ResultSet ticketRs = ticketStmt.executeQuery()) {

            while (ticketRs.next()) {
                int ticketId = ticketRs.getInt("id");
                List<Product> products = getProductsForTicket(ticketId, saleQuery);
                Ticket ticket = new Ticket(products);
                ticketsMap.put(ticketId, ticket);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving tickets from database: " + e.getMessage());
        }

        return ticketsMap;
    }

    private List<Product> getProductsForTicket(int ticketId, String saleQuery) {
        List<Product> products = new ArrayList<>();
        String productQuery = "SELECT * FROM Product WHERE id = ?";

        try (PreparedStatement saleStmt = connection.prepareStatement(saleQuery)) {
            saleStmt.setInt(1, ticketId);
            try (ResultSet saleRs = saleStmt.executeQuery()) {
                while (saleRs.next()) {
                    int productId = saleRs.getInt("product_id");
                    products.add(fetchProductById(productId, productQuery));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products for ticket: " + e.getMessage());
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

    private Product fetchProductById(int productId, String productQuery) throws SQLException {
        try (PreparedStatement productStmt = connection.prepareStatement(productQuery)) {
            productStmt.setInt(1, productId);
            try (ResultSet productRs = productStmt.executeQuery()) {
                if (productRs.next()) {
                    double price = productRs.getDouble("price");
                    String productType = productRs.getString("product_type");
                    Double height = productRs.getDouble("height");
                    String color = productRs.getString("color");
                    String material = productRs.getString("material");

                    Product product = createProduct(productType, price, height, color, material);
                    product.setId(productId);
                    return product;
                }
            }
        }
        return null;
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
}