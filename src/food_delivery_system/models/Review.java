package food_delivery_system.models;

public class Review {
    private static int idGenerator = 0;
    private final int id;
    private final String comment;
    private final int rating;
    private final Customer customer;

    public Review(Customer customer, String comment, int rating) {
        this.id = ++idGenerator;
        this.customer = customer;
        this.comment = comment;
        this.rating = rating;
    }

    public int getId() { return id; }
    public String getComment() { return comment; }
    public int getRating() { return rating; }
    public Customer getCustomer() { return customer; }
}
