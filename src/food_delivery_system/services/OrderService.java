package food_delivery_system.services;

import food_delivery_system.models.Order;
import food_delivery_system.models.Customer;
import food_delivery_system.models.Restaurant;
import food_delivery_system.models.Product;
import food_delivery_system.models.Driver;
import food_delivery_system.models.OrderStatus;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class OrderService {
    private Map<Integer, Order> allOrders;

    public OrderService() {
        this.allOrders = new HashMap<>();
    }
    
    public Order placeOrder(Customer customer, Restaurant restaurant, List<Product> products, double deliveryFee) {
        Order newOrder = new Order(customer, restaurant, products, deliveryFee);
        allOrders.put(newOrder.getId(), newOrder);
        
        System.out.println("✅ Comanda #" + newOrder.getId() + " a fost plasată cu succes de către " + customer.getFullName() + "!");
        System.out.println("💰 Total de plată estimat: " + newOrder.calculateTotalToPay() + " RON");
        
        return newOrder;
    }

    public Order getOrderById(int orderId) {
        return allOrders.get(orderId); // Căutare O(1)
    }

    public Order getOrderByCustomerAndRestaurant(Customer customer, Restaurant restaurant) {
        for (Order order : allOrders.values()) {
            if (order.getCustomer().equals(customer) && order.getRestaurant().equals(restaurant)) {
                return order;
            }
        }
        return null;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(allOrders.values());
    }

    public List<Order> getOrderHistoryForCustomer(int customerId) {
        List<Order> orders = new ArrayList<>();
        for (Order order : allOrders.values()) {
            if (order.getCustomer().getId() == customerId) {
                orders.add(order);
            }
        }
        return orders;
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("❌ Comanda cu ID-ul " + orderId + " nu există!");
        }
        
        order.setStatus(newStatus);
        System.out.println("🔄 Statusul comenzii #" + orderId + " a fost actualizat la: " + newStatus);
    }

    public void assignDriverToOrder(int orderId, Driver driver) {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("❌ Comanda cu ID-ul " + orderId + " nu există!");
        }
        if (driver == null) {
            throw new IllegalArgumentException("❌ Șoferul nu poate fi null!");
        }

        order.setDriver(driver);
        // Odată ce are șofer, comanda logic ar trebui să fie pe drum
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        System.out.println("🚗 Șoferul " + driver.getFullName() + " a preluat comanda #" + orderId + " (" + order.getRestaurant().getName() + " -> " + order.getCustomer().getFullName() + ").");
    }
}
