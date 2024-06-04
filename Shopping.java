//Project for Computer Programming 1 Class

import java.util.*;

class User {
    private int userID;
    private String username;
    private List<Product> cart;

    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;
        this.cart = new ArrayList<>();
    }

    public void addToCart(Product product) {
        cart.add(product);
        System.out.println(product.getProductName() + " added to cart.");
    }

    public void removeFromCart(Product product) {
        if (cart.remove(product)) {
            System.out.println(product.getProductName() + " removed from cart.");
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    public void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }

        double totalAmount = calculateTotalAmount();
        System.out.println("Checkout Summary for " + username + ":");
        for (Product product : cart) {
            System.out.println("- " + product.getProductName() + ": $" + product.getPrice());
        }
        System.out.println("Total Amount: $" + totalAmount);
        boolean paymentSuccess = PaymentGateway.processPayment(this, totalAmount);
        if (paymentSuccess) {
            cart.clear();
            System.out.println("Checkout successful!");
        } else {
            System.out.println("Checkout failed. Payment was not processed.");
        }
    }

    private double calculateTotalAmount() {
        double totalAmount = 0;
        for (Product product : cart) {
            totalAmount += product.getPrice();
        }
        return totalAmount;
    }

    public String getUsername() {
        return username;
    }

    public int getUserID() {
        return userID;
    }

    public List<Product> getCart() {
        return cart;
    }
}

class Product {
    private int productID;
    private String productName;
    private double price;
    private int stockCount;

    public Product(int productID, String productName, double price, int stockCount) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockCount = stockCount;
    }

    public boolean buyProduct(int count) {
        if (count <= stockCount) {
            stockCount -= count;
            return true;
        } else {
            System.out.println("Insufficient stock for " + productName);
            return false;
        }
    }

    public void getProductDetails() {
        System.out.println("Product ID: " + productID);
        System.out.println("Product Name: " + productName);
        System.out.println("Price: $" + price);
        System.out.println("Stock Count: " + stockCount);
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }
}
class Seller {
    private int sellerID;
    private String storeName;
    private List<Product> productsListed;

    public Seller(int sellerID, String storeName) {
        this.sellerID = sellerID;
        this.storeName = storeName;
        this.productsListed = new ArrayList<>();
    }

    public void listProduct(Product product) {
        productsListed.add(product);
        System.out.println(product.getProductName() + " listed in " + storeName);
    }

    public void removeProduct(Product product) {
        if (productsListed.remove(product)) {
            System.out.println(product.getProductName() + " removed from " + storeName);
        } else {
            System.out.println("Product not found in " + storeName);
        }
    }

    public double getSales() {
        double totalSales = 0;
        for (Product product : productsListed) {
            totalSales += product.getPrice();
        }
        return totalSales;
    }
}

class PaymentGateway {
    private static List<String> transactions = new ArrayList<>();

    public static boolean processPayment(User user, double amount) {
        boolean paymentSuccess = Math.random() < 0.8;
        if (paymentSuccess) {
            transactions.add("Payment of $" + amount + " processed for user " + user.getUsername());
        }
        return paymentSuccess;
    }

    public static void refund(User user, double amount) {
        transactions.add("Refund of $" + amount + " issued for user " + user.getUsername());
        System.out.println("Refund processed successfully.");
    }

    public static void getTransactionDetails() {
        System.out.println("Transaction History:");
        for (String transaction : transactions) {
            System.out.println("- " + transaction);
        }
    }
}

public class Shopping {
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static List<Product> availableProducts = new ArrayList<>();
    private static List<Seller> sellers = new ArrayList<>();

    public static void main(String[] args) {
        initializeSampleData();
        menu();
    }

    private static void initializeSampleData() {
        User user1 = new User(1, "Alice");
        User user2 = new User(2, "Bob");
        users.add(user1);
        users.add(user2);

        Product product1 = new Product(101, "Laptop", 999.99, 10);
        Product product2 = new Product(102, "Phone", 499.99, 20);
        Product product3 = new Product(103, "Headphones", 79.99, 30);
        availableProducts.add(product1);
        availableProducts.add(product2);
        availableProducts.add(product3);

        Seller seller1 = new Seller(201, "Electronics Store");
        seller1.listProduct(product1);
        seller1.listProduct(product2);
        sellers.add(seller1);
    }

    private static void menu() {
        while (true) {
            System.out.println("\nWelcome to Online Shopping Platform");
            System.out.println("1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Remove Product from Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 5) {
                System.out.println("Exiting...");
                break;
            }

            performAction(choice);
        }
    }

    private static void performAction(int choice) {
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                addProductToCart();
                break;
            case 3:
                removeProductFromCart();
                break;
            case 4:
                checkout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewProducts() {
        System.out.println("\nAvailable Products:");
        for (Product product : availableProducts) {
            product.getProductDetails();
        }
    }

    private static void addProductToCart() {
        System.out.print("Enter product ID to add to cart: ");
        int productID = scanner.nextInt();
        scanner.nextLine();

        Product selectedProduct = findProductById(productID);
        if (selectedProduct != null) {
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();
            if (selectedProduct.buyProduct(quantity)) {
                User currentUser = getCurrentUser();
                currentUser.addToCart(selectedProduct);
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void removeProductFromCart() {
        System.out.print("Enter product ID to remove from cart: ");
        int productID = scanner.nextInt();
        scanner.nextLine();

        Product selectedProduct = findProductById(productID);
        if (selectedProduct != null) {
            User currentUser = getCurrentUser();
            currentUser.removeFromCart(selectedProduct);
        } else {
            System.out.println("Product not found in available products.");
        }
    }

    private static void checkout() {
        User currentUser = getCurrentUser();
        if (currentUser != null && !currentUser.getCart().isEmpty()) {
            currentUser.checkout();
        } else {
            System.out.println("Cart is empty.");
        }
    }

    private static Product findProductById(int productID) {
        for (Product product : availableProducts) {
            if (product.getProductID() == productID) {
                return product;
            }
        }
        return null;
    }

    private static User getCurrentUser() {
        System.out.print("Enter user ID: ");
        int userID = scanner.nextInt();
        scanner.nextLine(); 

        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        System.out.println("User not found.");
        return null;
    }
}
