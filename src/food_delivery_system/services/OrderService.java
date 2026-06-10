package food_delivery_system.services;

import food_delivery_system.config.DatabaseHelper;
import food_delivery_system.models.Customer;
import food_delivery_system.models.Driver;
import food_delivery_system.models.Order;
import food_delivery_system.models.OrderStatus;
import food_delivery_system.models.Product;
import food_delivery_system.models.Restaurant;

import java.util.List;

public class OrderService {

    private final DatabaseHelper db = DatabaseHelper.getInstance();
    private final UserService userService;
    private final RestaurantService restaurantService;

    public OrderService(UserService userService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    // CREATE

    public Order placeOrder(Customer customer, Restaurant restaurant, List<Product> products, double deliveryFee) {
        Order newOrder = new Order(customer, restaurant, products, deliveryFee);

        // INSERT order into DB
        int orderId = db.executeUpdate(
            "INSERT INTO orders (customer_id, restaurant_id, driver_id, status, delivery_fee, total_price, discount) " +
            "VALUES (?, ?, NULL, ?, ?, ?, ?)",
            customer.getId(), restaurant.getId(),
            newOrder.getStatus().name(),
            newOrder.getDeliveryFee(),
            newOrder.getTotalPrice(),
            newOrder.getDiscount()
        );
        newOrder.setId(orderId);

        // INSERT each product into order_items
        for (Product p : products) {
            db.executeUpdate(
                "INSERT INTO order_items (order_id, product_id) VALUES (?, ?)",
                orderId, p.getId()
            );
        }

        System.out.println("✅ Comanda #" + orderId + " plasată de " + customer.getFullName() + "!");
        System.out.println("💰 Total: " + newOrder.calculateTotalToPay() + " RON");
        AuditService.getInstance().logAction("PLACE_ORDER");

        return newOrder;
    }

    // READ

    public Order getOrderById(int orderId) {
        List<Order> results = db.executeQuery(
            "SELECT * FROM orders WHERE id = ?",
            rs -> buildOrder(rs), orderId
        );
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Order> getAllOrders() {
        return db.executeQuery(
            "SELECT * FROM orders",
            rs -> buildOrder(rs)
        );
    }

    public List<Order> getOrderHistoryForCustomer(int customerId) {
        return db.executeQuery(
            "SELECT * FROM orders WHERE customer_id = ?",
            rs -> buildOrder(rs), customerId
        );
    }

    // UPDATE

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Comanda cu ID-ul " + orderId + " nu există!");

        db.executeUpdate(
            "UPDATE orders SET status = ? WHERE id = ?",
            newStatus.name(), orderId
        );
        System.out.println("🔄 Statusul comenzii #" + orderId + " actualizat la: " + newStatus);
        AuditService.getInstance().logAction("UPDATE_ORDER_STATUS");
    }

    public void assignDriverToOrder(int orderId, Driver driver) {
        if (driver == null)
            throw new IllegalArgumentException("Șoferul nu poate fi null!");

        Order order = getOrderById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Comanda cu ID-ul " + orderId + " nu există!");

        db.executeUpdate(
            "UPDATE orders SET driver_id = ?, status = ? WHERE id = ?",
            driver.getId(), OrderStatus.OUT_FOR_DELIVERY.name(), orderId
        );
        System.out.println("🚗 " + driver.getFullName() + " a preluat comanda #" + orderId + ".");
        AuditService.getInstance().logAction("ASSIGN_DRIVER");
    }

    // DELETE

    public void deleteOrder(int orderId) {
        // Cascades to order_items via FK ON DELETE CASCADE
        db.executeUpdate("DELETE FROM orders WHERE id = ?", orderId);
        System.out.println("✅ Comanda #" + orderId + " a fost ștearsă.");
    }

    // Helper — reconstructs an Order object from a ResultSet row

    private Order buildOrder(java.sql.ResultSet rs) throws java.sql.SQLException {
        int orderId       = rs.getInt("id");
        int customerId    = rs.getInt("customer_id");
        int restaurantId  = rs.getInt("restaurant_id");
        int driverId      = rs.getInt("driver_id");
        String statusStr  = rs.getString("status");
        double deliveryFee = rs.getDouble("delivery_fee");

        Customer   customer   = (Customer)   userService.getUserById(customerId);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        // Load the products for this order
        List<Product> products = db.executeQuery(
            "SELECT p.* FROM products p JOIN order_items oi ON p.id = oi.product_id WHERE oi.order_id = ?",
            prs -> new Product(
                prs.getInt("id"),
                prs.getString("name"),
                prs.getString("description"),
                prs.getDouble("price")
            ), orderId
        );

        Order order = new Order(orderId, customer, restaurant, products, deliveryFee);
        order.setStatus(OrderStatus.valueOf(statusStr));

        // Attach driver if one is assigned

        if (driverId != 0) {
            Driver driver = (Driver) userService.getUserById(driverId);
            if (driver != null) order.setDriver(driver);
        }

        return order;
    }
}
