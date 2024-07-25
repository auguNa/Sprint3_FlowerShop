package Nivel1_txt_persistance.menu;

import Nivel1_txt_persistance.exception.InvalidInputException;
import Nivel1_txt_persistance.florist_management.FloristManagement;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    private final nivel_1.factory.ProductFactory factory;
    Scanner sc = new Scanner(System.in);
    private FloristManagement florist;

    public MainMenu() {
        factory = nivel_1.factory.ProductFactory.getInstance();
    }

    public void start() {
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
                        if (florist == null) {
                            System.out.println("Please create a florist first.");
                        } else {
                            new StockManagementMenu(florist, factory).manageStock(sc);
                        }
                        break;
                    case 3:
                        if (florist == null) {
                            System.out.println("Please create a florist first.");
                        } else {
                            new SalesManagementMenu(florist).manageSales();
                        }
                        break;
                    case 0:
                        System.out.println("Application ended.");
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
            florist = new FloristManagement(name);
            System.out.println("Florist '" + name + "' created ");
        } else {
            System.out.println("Florist already exists: " + florist.getName());
        }
    }
}
