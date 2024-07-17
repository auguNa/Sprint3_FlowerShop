package Nivel1_txt_persistance.menu;

import Nivel1_txt_persistance.florist.Florist;
import java.util.Scanner;

public class MainMenu {
    Scanner sc = new Scanner(System.in);
    private Florist florist;
    private final nivel_1.factory.ProductFactory factory;

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
                        new nivel_1.menu.StockManagementMenu(florist, factory).manageStock(sc);
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
        } while (option != 0);
        sc.close();
    }

    private void createFlorist() {
        if (florist == null) {
            System.out.println("Enter the name of the florist:");
            String name = sc.nextLine();
            florist = new Florist(name);
            System.out.println("Florist '" + name + "' created ");
        } else {
            System.out.println("Florist already exists: " + florist.getName());
        }
    }
}
