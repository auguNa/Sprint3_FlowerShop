package Nivel1_txt_persistance.florist;

import Nivel1_txt_persistance.product_management.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Florist {
    private static final String STOCK_FILE = "stock.txt";
    private static final String SALES_FILE = "sales.txt";
    private String name;
    private ArrayList<Product> stock;
    private ArrayList<Ticket> sales;

    public Florist(String name) {
        this.stock = new ArrayList<>();
        this.sales = new ArrayList<>();
        this.name = name;
        loadStock();
        loadSales();
    }

    public String getName() {
        return name;
    }

    public List<Product> getStock() {
        return stock;
    }

    public void addProduct(Product product) {
        stock.add(product);
        saveStock();
    }

    public void removeProduct(Product product) {
        stock.remove(product);
        saveStock();
    }

    public void addSale(Ticket ticket) {
        sales.add(ticket);
        saveSales();
    }

    public List<Ticket> getSales() {
        return sales;
    }

    public double getTotalStockValue() {
        return stock.stream().mapToDouble(Product::getPrice).sum();
    }

    public double getTotalSalesValue() {
        return sales.stream().mapToDouble(Ticket::getTotalValue).sum();
    }

    private void saveStock() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STOCK_FILE))) {
            oos.writeObject(stock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStock() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STOCK_FILE))) {
            stock = (ArrayList<Product>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Stock file not found. Starting with an empty stock.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveSales() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SALES_FILE))) {
            out.writeObject(sales);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSales() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SALES_FILE))) {
            sales = (ArrayList<Ticket>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Sales file not found. Starting with no sales.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
