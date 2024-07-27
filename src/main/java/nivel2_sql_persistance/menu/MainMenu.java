package nivel2_sql_persistance.menu;

import nivel2_sql_persistance.dao.SalesDAO;
import nivel2_sql_persistance.exception.InvalidInputException;
import nivel2_sql_persistance.factory.ProductFactory;
import nivel2_sql_persistance.florist_management.FloristManagement;
import nivel2_sql_persistance.connection.DBConnection;
import nivel2_sql_persistance.dao.*;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {

    private final ProductFactory factory;
    private final Scanner sc;
    private FloristManagement florist;
    private FloristDAO floristDAO;
    private StockDAO stockDAO;
    private SalesDAO salesDAO;

    public MainMenu() {
        factory = ProductFactory.getInstance();
        sc = new Scanner(System.in);
        DBConnection.connect();

        Connection connection = DBConnection.getConnection();
        floristDAO = new FloristDAO(connection);
        stockDAO = new StockDAO(connection);
        salesDAO = new SalesDAO(connection);
    }

    public void start() {
        loadFloristFromDatabase();

        int option;
        do {
            System.out.println("1.- Create Florist");
            System.out.println("2.- Stock Management");
            System.out.println("3.- Sales Management");
            System.out.println("0.- End the Application");
            try {
                option = sc.nextInt();
                sc.nextLine();
                switch (option) {
                    case 1:
                        createFlorist();
                        break;
                    case 2:
                        loadStockMenu();
                        break;
                    case 3:
                        loadSalesMenu();
                        break;
                    case 0:
                        exitApp();
                        break;
                    default:
                        System.out.println("Invalid Option");
                }
            } catch (InputMismatchException e) {
                sc.nextLine();
                handleInvalidInput(new InvalidInputException("Invalid input. Please enter a number."));
                option = -1;
            }
        } while (option != 0);
        sc.close();
    }

    private void handleInvalidInput(InvalidInputException e) {
        System.out.println("Error: " + e.getMessage());
    }

    private void createFlorist() {
        if (florist == null) {
            System.out.println("Enter the name of the florist:");
            String name = sc.nextLine();
            floristDAO.createFloristInDatabase(name);
            florist = new FloristManagement(name, stockDAO, salesDAO);
        } else {
            System.out.println("Florist already exists: " + florist.getName());
        }
    }

    private void loadFloristFromDatabase() {
        String floristName = floristDAO.getFloristNameFromDatabase();
        if (!floristName.isEmpty()) {
            florist = new FloristManagement(floristName, stockDAO, salesDAO);
            System.out.println("Loaded florist: " + florist.getName());
        }else{
            System.out.println("Please create a florist first.");
        }
    }

    private void loadStockMenu() {
        if (florist == null) {
            System.out.println("Please, create a florist first.");
        } else {
            new StockManagementMenu(florist, factory).manageStock(sc);
        }
    }

    private void loadSalesMenu() {
        if (florist == null) {
            System.out.println("Please create a florist first.");
        } else {
            new SalesManagementMenu(florist).manageSales();
        }
    }

    private void exitApp(){
        System.out.println("Application ended.");
        DBConnection.close();
    }
}