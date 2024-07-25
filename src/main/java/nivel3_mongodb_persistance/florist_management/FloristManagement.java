package nivel3_mongodb_persistance.florist_management;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import nivel3_mongodb_persistance.connection.MongoDBConnection;
import nivel3_mongodb_persistance.persistence.Ticket;
import nivel3_mongodb_persistance.product_management.Decoration;
import nivel3_mongodb_persistance.product_management.Flower;
import nivel3_mongodb_persistance.product_management.Product;
import nivel3_mongodb_persistance.product_management.Tree;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FloristManagement {
    private MongoDatabase database = MongoDBConnection.getDatabase();
    private MongoCollection<Document> products = database.getCollection("products");
    private MongoCollection<Document> tickets = database.getCollection("tickets");

    private String name;
    private ArrayList<Product> stock;
    private ArrayList<Ticket> sales;

    public FloristManagement(String name) {
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

        Document productToAdd = new Document()
                .append("counter", product.getCounter())
                .append("id", product.getId())
                .append("price", product.getPrice())
                .append("attribute", product.getAttribute());

        products.insertOne(productToAdd);
        stock.add(product);
    }

    public void removeProduct(Product product) {
        Document productToRemove = new Document("id", product.getId());
        products.deleteOne(productToRemove);
        stock.remove(product);
    }

    public void addSale(Ticket ticket) {

        sales.add(ticket);
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



    private void loadStock() {
        try (MongoCursor<Document> cursor = products.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int id = doc.getInteger("id");
                double price = doc.getDouble("price");
                String attribute = doc.getString("attribute");

                if(doc.getString("type").equals("tree")) {
                    Product product = new Tree(price, 0);
                    stock.add(product);
                } else if(doc.getString("type").equals("flower")) {
                    Product product = new Flower(price, attribute);
                    stock.add(product);
                } else if(doc.getString("type").equals("decoration")) {
                    Product product = new Decoration(price, null);
                    stock.add(product);
                }
            }
        }
    }

    private void loadSales() {

    }
}