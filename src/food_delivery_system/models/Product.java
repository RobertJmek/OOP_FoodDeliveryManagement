package food_delivery_system.models;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;


    public Product(String name, String description, double price) {
        setName(name);
        this.description = description;
        setPrice(price);
        this.stock = 0;
        this.imageUrl = null;
    }

    public Product(int id, String name, String description, double price) {
        this.id = id;
        setName(name);
        this.description = description;
        setPrice(price);
        this.stock = 0;
        this.imageUrl = null;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stocul nu poate fi negativ!");
        }
        this.stock = stock;
    }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Pretul nu poate fi negativ!");
        }
        this.price = price;
    }
    public void setDescription(String description) { this.description = description; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi null sau gol!");
        }
        this.name = name.trim();
    }

}
