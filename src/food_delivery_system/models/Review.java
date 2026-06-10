package food_delivery_system.models;

public class Review {
    private int id;
    private final String comment;
    private final int rating;
    private final Customer customer;

    public Review(Customer customer, String comment, int rating) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer nu poate fi null!");
        }
        if (rating < 1 || rating > 5) {
             throw new IllegalArgumentException("Rating-ul trebuie sa fie intre 1 si 5!");
        }
        this.customer = customer;
        this.comment = comment;
        this.rating = rating;
    }

    public Review(int id, Customer customer, String comment, int rating) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer nu poate fi null!");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating-ul trebuie sa fie intre 1 si 5!");
        }
        this.id = id;
        this.customer = customer;
        this.comment = comment;
        this.rating = rating;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public String getComment() { return comment; }
    public int getRating() { return rating; }
    public Customer getCustomer() { return customer; }
}
