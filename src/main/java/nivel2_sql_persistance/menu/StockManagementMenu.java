package nivel2_sql_persistance.menu;

import nivel2_sql_persistance.exception.InvalidDecorationTypeException;
import nivel2_sql_persistance.factory.ProductFactory;
import nivel2_sql_persistance.florist_management.FloristManagement;
import nivel2_sql_persistance.product_management.Decoration;
import nivel2_sql_persistance.product_management.Flower;
import nivel2_sql_persistance.product_management.Product;
import nivel2_sql_persistance.product_management.Tree;

import java.util.*;

public class StockManagementMenu {
    private FloristManagement florist;
    private ProductFactory factory;

    public StockManagementMenu(FloristManagement florist, ProductFactory factory) {
        this.florist = florist;
        this.factory = factory;
    }

    public void manageStock(Scanner sc) {
        int option;
        do {
            System.out.println("Stock Management:");
            System.out.println("1.- Add Tree");
            System.out.println("2.- Add Flower");
            System.out.println("3.- Add Decoration");
            System.out.println("4.- Remove Tree");
            System.out.println("5.- Remove Flower");
            System.out.println("6.- Remove Decoration");
            System.out.println("7.- Print Stock");
            System.out.println("8.- Print Stock with Quantities");
            System.out.println("9.- Print Total Stock Value");
            System.out.println("0.- Back to Main Menu");

            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    addTree(sc);
                    break;
                case 2:
                    addFlower(sc);
                    break;
                case 3:
                    addDecoration(sc);
                    break;
                case 4:
                    removeProduct(sc, Tree.class);
                    break;
                case 5:
                    removeProduct(sc, Flower.class);
                    break;
                case 6:
                    removeProduct(sc, Decoration.class);
                    break;
                case 7:
                    printStock();
                    break;
                case 8:
                    printStockWithQuantities();
                    break;
                case 9:
                    printTotalStockValue();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        } while (option != 0);
    }

    private void addTree(Scanner sc) {
        System.out.println("Enter price:");
        double price = sc.nextDouble();
        System.out.println("Enter height:");
        double height = sc.nextDouble();
        Product tree = factory.createProduct("tree", price, String.valueOf(height));
        florist.addProduct(tree);
        System.out.println("Tree added.");
    }

    private void addFlower(Scanner sc) {
        System.out.println("Enter price:");
        double price = sc.nextDouble();
        System.out.println("Enter color:");
        String color = sc.next();
        Product flower = factory.createProduct("flower", price, color);
        florist.addProduct(flower);
        System.out.println("Flower added.");
    }

    private void addDecoration(Scanner sc) {
        try {
            System.out.println("Enter price:");

            double price = sc.nextDouble();
            System.out.println("Enter material (wood/plastic):");
            String material = sc.next();
            if (!material.equalsIgnoreCase("wood") && !material.equalsIgnoreCase("plastic")) {
<<<<<<< HEAD:src/main/java/Nivel1_txt_persistance/menu/StockManagementMenu.java
                throw new InvalidDecorationType("Invalid material type: " + material);
=======
                throw new InvalidDecorationTypeException("Invalid material type: " + material);
>>>>>>> sql_persistance:src/main/java/nivel2_sql_persistance/menu/StockManagementMenu.java
            }
            Product decoration = factory.createProduct("decoration", price, material);
            florist.addProduct(decoration);
            System.out.println("Decoration added.");
<<<<<<< HEAD:src/main/java/Nivel1_txt_persistance/menu/StockManagementMenu.java
        } catch (InvalidDecorationType e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
=======
        } catch (InvalidDecorationTypeException e) {
>>>>>>> sql_persistance:src/main/java/nivel2_sql_persistance/menu/StockManagementMenu.java
            System.out.println("Invalid input. Please enter a valid number for the price.");
            sc.next();
        }
    }

    private void removeProduct(Scanner sc, Class<? extends Product> type) {
        System.out.println("Enter the ID of the product to remove:");
        int id = sc.nextInt();
        Optional<Product> product = florist.getStock().stream()
                .filter(p -> p.getId() == id && type.isInstance(p))
                .findFirst();
        if (product.isPresent()) {
            florist.removeProduct(product.get());
            System.out.println(type.getSimpleName() + " with ID " + id + " removed.");
        } else {
            System.out.println(type.getSimpleName() + " with ID " + id + " not found.");
        }
    }

    private void printStock() {
        List<Product> stock = florist.getStock();
        if (stock.isEmpty()) {
            System.out.println("No products in stock.");
        } else {
            stock.forEach(System.out::println);
        }
    }

    private void printStockWithQuantities() {
        Map<String, Long> productCounts = new HashMap<>();
        for (Product product : florist.getStock()) {
            String key = product.getClass().getSimpleName();
            productCounts.put(key, productCounts.getOrDefault(key, 0L) + 1);
        }
        productCounts.forEach((key, count) -> System.out.println(key + ": " + count));
    }

    private void printTotalStockValue() {
        double totalValue = florist.getTotalStockValue();
        System.out.println("Total stock value: €" + totalValue);
    }
}