package nivel2_sql_persistance.florist_management;

import nivel2_sql_persistance.connection.DBConnection;
import nivel2_sql_persistance.dao.SalesDAO;
import nivel2_sql_persistance.dao.StockDAO;
import nivel2_sql_persistance.persistence.Ticket;
import nivel2_sql_persistance.product_management.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FloristManagement {
    private Connection connection;
    private String name;
    private List<Product> stock;
    private List<Ticket> sales;
    private StockDAO stockDAO;
    private SalesDAO salesDAO;

    public FloristManagement(String name, StockDAO stockDAO, SalesDAO salesDAO) {
        this.connection = DBConnection.getConnection();
        this.name = name;
        this.stockDAO = stockDAO;
        this.salesDAO = salesDAO;
        this.stock = new ArrayList<>();
        this.sales = new ArrayList<>();
        loadStock();
        loadSales();
    }

    public String getName() {
        return name;
    }

    public List<Product> getStock() {
        loadStock();
        return stock;
    }

    public List<Ticket> getSales() {
        loadSales();
        return sales;
    }

    public void addProduct(Product product) {
        stock.add(product);
        try {
            saveStock(product);
        } catch (SQLException e) {
            System.err.println("Error saving product to the database: " + e.getMessage());
        }
    }

    private void saveStock(Product product) throws SQLException {
        stockDAO.saveNewProduct(product);
    }

    public void removeProduct(Product product) {
        loadStock();
        stock.remove(product);
        try {
            stockDAO.deleteProduct(product.getId());
        } catch (SQLException e) {
            System.err.println("Error deleting product from the database: " + e.getMessage());
        }
    }

    public void addSale(Ticket ticket) {
        if (ticket != null) {
            sales.add(ticket);
            try {
                salesDAO.saveSale(ticket);
                updateStockAfterSale(ticket);
            } catch (SQLException e) {
                System.err.println("Error saving ticket to the database: " + e.getMessage());
            }
        } else {
            System.out.println("Ticket cannot be null.");
        }
    }

    private void updateStockAfterSale(Ticket ticket) {
        for (Product product : ticket.getProducts()) {
            try {
                stockDAO.updateStockValue(product.getId());
            } catch (SQLException e) {
                System.err.println("Error updating stock after sale: " + e.getMessage());
            }
        }
    }

    public double getTotalSalesValue() {
        return sales.stream().mapToDouble(Ticket::getTotalValue).sum();
    }

    public double getTotalStockValue() {
        return stock.stream().mapToDouble(Product::getPrice).sum();
    }

    public void loadStock() {
        if (stockDAO != null) {
            List<Product> products = stockDAO.getStockFromDatabase();
            this.stock.clear();
            this.stock.addAll(products);
        }
    }

    public void loadSales() {
        if (salesDAO != null) {
            Map<Integer, Ticket> ticketsMap = salesDAO.getTicketsFromDatabase();
            this.sales.clear();
            this.sales.addAll(ticketsMap.values());
        }
    }

    public void showListOfOldPurchases() {
        if (sales.isEmpty()) {
            System.out.println("No sales records.");
        } else {
            sales.forEach(ticket -> System.out.println(ticket.toString()));
        }
    }
}