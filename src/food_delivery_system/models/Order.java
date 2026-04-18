package food_delivery_system.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Order {
    private static int idGenerator = 0;
    private final int id;
    private final Customer customer;
    private final Restaurant restaurant;
    private double totalPrice;
    private double discount;
    private double deliveryFee;
    private Driver driver;
    private OrderStatus status;
    private List<Product> products;


    public Order(Customer customer, Restaurant restaurant, List<Product> products, double deliveryFee) {
        if (customer == null) {
            throw new IllegalArgumentException("Comanda trebuie să aibă un client valid!");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Comanda trebuie să fie asociată unui restaurant!");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Comanda nu poate fi goala!");
        }
        if (deliveryFee < 0) {
            throw new IllegalArgumentException("Taxa de livrare nu poate fi negativa!");
        }

        this.id = ++idGenerator;
        this.customer = customer;
        this.restaurant = restaurant;
        this.products = new ArrayList<>(products);
        this.discount = 0.0;
        this.deliveryFee = deliveryFee;

        this.totalPrice = calculateTotalProducts(products); //  calcularea totalului comenzii
        this.status = OrderStatus.PENDING;
    }

    private double calculateTotalProducts (List<Product> products) {
        double sum = 0;
        for (Product p : products) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void setDiscount(double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discountul nu poate fi negativ sau mai mare de 100!");
        }
        this.discount = discount;
    }

    public void setDeliveryFee(double deliveryFee) {
        if (deliveryFee < 0) {
            throw new IllegalArgumentException("Taxa de livrare nu poate fi negativa!");
        }
        this.deliveryFee = deliveryFee;
    }

    public double calculateTotalToPay() {
        return totalPrice - (discount/100.0 * totalPrice) + deliveryFee;
    }

    public void setDriver(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driverul nu poate fi null!");
        }
        this.driver = driver;
    }

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Statusul nu poate fi null!");
        }
        this.status = status;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Produsul nu poate fi null!");
        }
        products.add(product);
        totalPrice += product.getPrice();
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Driver getDriver() {
        return driver;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public double getDiscount() {
        return discount;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }


}
