import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fawry{
    // Product class to define products with name, price, quantity, expiry, shippable status, and weight
    public static class Product {
        private String name;
        private double price;
        private int quantity;
        private boolean isExpired;
        private boolean shippable;
        private double weight; // in kg

        public Product(String name, double price, int quantity, boolean isExpired, boolean shippable, double weight) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.isExpired = isExpired;
            this.shippable = shippable;
            this.weight = weight;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public boolean isExpired() { return isExpired; }
        public boolean isShippable() { return shippable; }
        public double getWeight() { return weight; }
        public void reduceQuantity(int qty) { this.quantity -= qty; }
    }

    // Cart class to manage items and their quantities
    public static class Cart {
        private HashMap<Product, Integer> items;

        public Cart() {
            this.items = new HashMap<>();
        }

        public void addItem(Product p, int qty) throws Exception {
            if (qty <= 0 || qty > p.getQuantity()) {
                throw new Exception("Invalid quantity: exceeds available stock or non-positive");
            }
            items.put(p, items.getOrDefault(p, 0) + qty);
            p.reduceQuantity(qty);
        }

        public HashMap<Product, Integer> getItems() { return items; }

        public double getSubtotal() {
            double total = 0;
            for (Product p : items.keySet()) {
                total += p.getPrice() * items.get(p);
            }
            return total;
        }
    }

    // Customer class to manage balance
    public static class Customer {
        private double balance;

        public Customer(double balance) {
            this.balance = balance;
        }

        public double getBalance() { return balance; }
        public void updateBalance(double amount) throws Exception {
            if (balance < amount) throw new Exception("Insufficient balance");
            balance -= amount;
        }
    }

    // ShippingService interface for shippable items
    public interface ShippingService {
        String getName();
        double getWeight();
    }

    // DefaultShipping class to implement ShippingService
    public static class DefaultShipping implements ShippingService {
        private Product product;

        public DefaultShipping(Product p) {
            this.product = p;
        }

        public String getName() { return product.getName(); }
        public double getWeight() { return product.getWeight(); }
    }

    // Checkout class to handle the checkout process
    public static class Checkout {
        public void checkout(Customer c, Cart cart) throws Exception {
            if (cart.getItems().isEmpty()) throw new Exception("Cart is empty");
            for (Product p : cart.getItems().keySet()) {
                if (p.isExpired() || cart.getItems().get(p) > p.getQuantity()) {
                    throw new Exception(p.getName() + " is expired or out of stock");
                }
            }
            List<Product> shippableItems = new ArrayList<>();
            for (Product p : cart.getItems().keySet()) {
                if (p.isShippable() && !p.isExpired() && cart.getItems().get(p) <= p.getQuantity()) {
                    shippableItems.add(p);
                }
            }

            double subtotal = cart.getSubtotal();
            double shippingFees = (shippableItems.isEmpty()) ? 0 : 30; // Flat fee for shippable items
            double total = subtotal + shippingFees;

            if (c.getBalance() < total) throw new Exception("Insufficient balance");

            c.updateBalance(total);

            // Send shippable items to ShippingService
            if (!shippableItems.isEmpty()) {
                List<ShippingService> shippingServices = new ArrayList<>();
                for (Product p : shippableItems) {
                    shippingServices.add(new DefaultShipping(p));
                }
                shipmentNotice(shippingServices);
            }

            // Print checkout receipt
            System.out.println("** Checkout receipt **");
            for (Product p : cart.getItems().keySet()) {
                int qty = cart.getItems().get(p);
                System.out.println(qty + "x " + p.getName() + " " + (qty * p.getPrice()));
            }
            System.out.println("Subtotal ___ " + subtotal);
            System.out.println("Shipping ___ " + shippingFees);
            System.out.println("Amount ___ " + total);
            System.out.println("Updated Balance ___ " + c.getBalance());
            System.out.println("END");
        }
    	// Print shipment notice and item details
        private void shipmentNotice(List<ShippingService> services) {
            System.out.println("** shipment Notice **");
            double totalWeight = 0;
            for (ShippingService s : services) {
                System.out.println("Item: " + s.getName() + ", Weight: " + (s.getWeight() * 1000) + "g");
                totalWeight += s.getWeight();
            }
            System.out.println("Total shipment weight: " + totalWeight + "kg");
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            Product cheese = new Product("Cheese", 100, 10, false, true, 0.4);
            Product biscuits = new Product("Biscuits", 150, 5, false, true, 0.7);
            Product scratchCard = new Product("ScratchCard", 50, 20, false, false, 0);

            Cart cart = new Cart();
            cart.addItem(cheese, 2);
            cart.addItem(biscuits, 1);
            cart.addItem(scratchCard, 3);

            Customer customer = new Customer(500);
            Checkout checkout = new Checkout();
            checkout.checkout(customer, cart);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
