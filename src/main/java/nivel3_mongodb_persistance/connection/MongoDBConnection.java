package nivel3_mongodb_persistance.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void connect() {
        String uri = "mongodb+srv://albamrquz3:<PASSWORD>L@flowershop.etspwlk.mongodb.net/?retryWrites=true&w=majority&appName=FlowerShop";

        if (mongoClient == null) {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("FlowerShop");
            System.out.println("Connected!");
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Connection not established. Call connect() first.");
        }
        System.out.println("DB created!");
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println("Disconnected!");
        }
    }
}
