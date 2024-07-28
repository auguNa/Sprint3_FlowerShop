package nivel3_mongodb_persistance.florist_management;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import nivel3_mongodb_persistance.connection.MongoDBConnection;
import nivel3_mongodb_persistance.persistence.Ticket;
import nivel3_mongodb_persistance.product_management.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FloristManagement {
    private final MongoDatabase database = MongoDBConnection.getDatabase();
    private final MongoCollection<Document> products = database.getCollection("products");
    private final MongoCollection<Document> tickets = database.getCollection("tickets");

    private final String name;
    private final ArrayList<Product> stock;
    private final ArrayList<Ticket> sales;

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

        try {
            String type = product.getClass().getSimpleName().toLowerCase();

            Document productToAdd = new Document()
                    .append("counter", product.getCounter())
                    .append("id", product.getId())
                    .append("price", product.getPrice())
                    .append("type", type)
                    .append("attribute", product.getAttribute());

            products.insertOne(productToAdd);
            stock.add(product);
        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private ObjectId getProductById(int id) {
        try {
            Bson filter = Filters.eq("id", id);
            Document productId = products.find(filter).first();

            if (productId != null) {
                return productId.getObjectId("_id");
            } else {
                System.out.println("Product not found with ID: " + id);
                return null;
            }

        } catch (Exception e) {
            System.out.println("Error retrieving product: " + e.getMessage());
            return null;
        }
    }

    public void removeProduct(Product product) {
        try {
            Document productToRemove = new Document("id", product.getId());
            products.deleteOne(productToRemove);
            stock.remove(product);
        } catch (Exception e) {
            System.out.println("Error removing product: " + e.getMessage());
        }
    }

    public void addSale(Ticket ticket) {
        if (ticket == null || ticket.getProducts() == null) {
            System.out.println("Adding a sale with a null ticket or null products list is not allowed.");
            return;
        }

        try {
            List<ObjectId> productObjectIds = ticket.getProducts().stream()
                    .map(product -> getProductById(product.getId()))
                    .filter(Objects::nonNull)
                    .toList();

            if (productObjectIds.isEmpty()) {
                System.out.println("No valid products found");
                return;
            }

            Document ticketToAdd = new Document("products", productObjectIds);
            tickets.insertOne(ticketToAdd);
            sales.add(ticket);
            products.deleteMany(Filters.in("_id", productObjectIds));
        } catch (Exception e) {
            System.out.println("Error adding sale: " + e.getMessage());
        }

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
        stock.clear();

        try (MongoCursor<Document> cursor = products.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int id = doc.getInteger("id");
                double price = doc.getDouble("price");
                String attribute = doc.getString("attribute");
                String type = doc.getString("type");

                if(doc.getString("type").equals("tree")) {
                    Product product = new Tree(price, Double.parseDouble(attribute));
                    stock.add(product);
                } else if(doc.getString("type").equals("flower")) {
                    Product product = new Flower(price, attribute);
                    stock.add(product);
                } else if(doc.getString("type").equals("decoration")) {
                    Product product = new Decoration(price, Objects.equals(attribute, "WOOD") ? Material.WOOD : Material.PLASTIC);
                    stock.add(product);
                }
            }
        }
    }

    public Product getProductByObjectId(ObjectId objectId) {
        try {
            Document productDoc = products.find(new Document("_id", objectId)).first();
            if (productDoc != null) {
                String type = productDoc.getString("type");
                double price = productDoc.getDouble("price");
                String attribute = productDoc.getString("attribute");

                return switch (type) {
                    case "tree" -> new Tree(price, Double.parseDouble(attribute));
                    case "flower" -> new Flower(price, attribute);
                    case "decoration" ->
                            new Decoration(price, Objects.equals(attribute, "WOOD") ? Material.WOOD : Material.PLASTIC);
                    default -> null;
                };
            } else {
                System.out.println("Product not found");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving product");
            return null;
        }
    }


    private void loadSales() {
        sales.clear();

        FindIterable<Document> salesDocuments = tickets.find();

        for (Document saleDoc : salesDocuments) {
            List<ObjectId> productObjectIds = saleDoc.getList("products", ObjectId.class);

            List<Product> productsList = new ArrayList<>();
            for (ObjectId objectId : productObjectIds) {
                Product product = getProductByObjectId(objectId);
                if (product != null) {
                    productsList.add(product);
                }
            }

            Ticket ticket = new Ticket(productsList);

            sales.add(ticket);
        }
    }
}