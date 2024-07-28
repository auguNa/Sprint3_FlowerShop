package nivel3_mongodb_persistance.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static final String URI = System.getenv("MONGODB_URI");
    private static final String DATABASE_NAME = "FlowerShop";

    public static void connect() {
        if (mongoClient == null) {
            try {
                if (URI == null || URI.isEmpty()) {
                    throw new IllegalStateException("MongoDB URI is not set or empty");
                }
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(DATABASE_NAME);

            } catch (Exception e) {
                throw new RuntimeException("Database connection error", e);
            }
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Connection not established. Call connect() first.");
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            try {
                mongoClient.close();
            } catch (Exception e) {
                System.out.println("Error closing MongoDB connection: " + e.getMessage());
            } finally {
                mongoClient = null;
                database = null;
            }
        }
    }
}
