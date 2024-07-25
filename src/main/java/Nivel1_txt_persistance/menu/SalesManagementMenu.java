package Nivel1_txt_persistance.menu;

import Nivel1_txt_persistance.florist_management.FloristManagement;
import Nivel1_txt_persistance.persistence.Ticket;
import Nivel1_txt_persistance.product_management.Product;

import java.math.BigDecimal;
import java.util.*;

public class SalesManagementMenu {
    Scanner sc = new Scanner(System.in);
    private FloristManagement florist;

    public SalesManagementMenu(FloristManagement florist) {
        this.florist = florist;
    }

    public void manageSales() {
        int option;
        do {
            System.out.println("Sales Management:");
            System.out.println("1.- Create Purchase Ticket");
            System.out.println("2.- Show List of Old Purchases");
            System.out.println("3.- View Total Money Earned from All Sales");
            System.out.println("0.- Back to Main Menu");

            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    createPurchaseTicket();
                    break;
                case 2:
                    showListOfOldPurchases();
                    break;
                case 3:
                    viewTotalMoneyEarnedFromAllSales();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        } while (option != 0);
    }

    private void createPurchaseTicket() {
        List<Product> purchasedProducts = new ArrayList<>();

        while (true) {
            System.out.println("Enter the type of product to purchase (tree/flower/decoration) or 'done' to finish:");
            String type = sc.nextLine();
            if (type.equalsIgnoreCase("done")) {
                break;
            }

            String attribute = "";
            switch (type.toLowerCase()) {
                case "tree":
                    System.out.println("Enter the height of the tree:");
                    attribute = sc.nextLine();
                    break;
                case "flower":
                    System.out.println("Enter the color of the flower:");
                    attribute = sc.nextLine();
                    break;
                case "decoration":
                    System.out.println("Enter the material of the decoration (WOOD/PLASTIC):");
                    attribute = sc.nextLine().toUpperCase();
                    break;
                default:
                    System.out.println("Invalid product type.");
                    continue;
            }

            System.out.println("Enter the quantity to purchase:");
            int quantity = sc.nextInt();
            sc.nextLine();

            boolean productsAvailable = checkStock(type, attribute, quantity);
            if (productsAvailable) {
                for (int i = 0; i < quantity; i++) {
                    Product product = getProductFromStock(type, attribute);
                    purchasedProducts.add(product);
                }
                removeProductsFromStock(type, attribute, quantity);
            } else {
                System.out.println("Not enough stock available for the specified product.");
            }
        }
        if (!purchasedProducts.isEmpty()) {
            Ticket ticket = new Ticket(purchasedProducts);
            florist.addSale(ticket);
            System.out.println("Purchase ticket created.");
            printTicketSummary(ticket);
        } else {
            System.out.println("No products purchased.");
        }
    }

    private void printTicketSummary(Ticket ticket) {
        System.out.println("\n--- Purchase Ticket Summary ---");
        Map<String, Integer> productSummary = new HashMap<>();

        for (Product product : ticket.getProducts()) {
            String productKey = product.getClass().getSimpleName() + " (" + product.getAttribute() + ")";
            productSummary.put(productKey, productSummary.getOrDefault(productKey, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : productSummary.entrySet()) {
            System.out.println("Product: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }

        System.out.println("Total Value: €" + ticket.getTotalValue());
        System.out.println("------------------------------\n");
    }

    private boolean checkStock(String type, String attribute, int quantity) {
        long count = florist.getStock().stream()
                .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(type)
                        && p.getAttribute().equalsIgnoreCase(attribute))
                .count();
        return count >= quantity;
    }

    private Product getProductFromStock(String type, String attribute) {
        return florist.getStock().stream()
                .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(type)
                        && p.getAttribute().equalsIgnoreCase(attribute))
                .findFirst()
                .orElse(null);
    }

    private void removeProductsFromStock(String type, String attribute, int quantity) {
        Iterator<Product> iterator = florist.getStock().iterator();
        int removedCount = 0;
        while (iterator.hasNext() && removedCount < quantity) {
            Product product = iterator.next();
            if (product.getClass().getSimpleName().equalsIgnoreCase(type)
                    && product.getAttribute().equalsIgnoreCase(attribute)) {
                iterator.remove();
                removedCount++;
            }
        }
    }

    private void showListOfOldPurchases() {
        List<Ticket> sales = florist.getSales();
        if (sales.isEmpty()) {
            System.out.println("No sales records.");
        } else {
            sales.forEach(System.out::println);
        }
    }

    private void viewTotalMoneyEarnedFromAllSales() {
        double totalSalesValue = florist.getTotalSalesValue();
        System.out.println("Total money earned from all sales: €" + totalSalesValue);
    }
}


