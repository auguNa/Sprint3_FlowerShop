package nivel2_sql_persistance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloristDAO {
    private Connection connection;

    public FloristDAO(Connection connection) {
        this.connection = connection;
    }

    public String getFloristNameFromDatabase() {
        String floristName = "";
        String query = "SELECT florist_name FROM florist WHERE id = 1";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                floristName = rs.getString("florist_name");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving florist name: " + e.getMessage());
        }
        return floristName;
    }

    public void createFloristInDatabase(String floristName) {
        String query = "INSERT INTO florist (florist_name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, floristName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Florist '" + floristName + "' created successfully.");
            } else {
                System.err.println("Failed to create florist.");
            }
        } catch (SQLException e) {
            System.err.println("Error creating florist: " + e.getMessage());
        }
    }
}
