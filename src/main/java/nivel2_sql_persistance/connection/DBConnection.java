package nivel2_sql_persistance.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/flower_shop";
    private static final String USER = "flower_shop_user";
    private static final String PASSWORD = "";

    static {
        loadJDBCDriver();
    }

    private static Connection connection;

    private DBConnection() {
    }

     private static void loadJDBCDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC driver.", e);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }

    public static void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.err.println("Error connecting database.");
        }
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Disconnected from database.");
            } catch (SQLException e) {
                System.err.println("Error disconnecting database.");
            }
        }
    }
}
