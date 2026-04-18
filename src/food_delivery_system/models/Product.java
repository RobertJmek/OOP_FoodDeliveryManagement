package food_delivery_system.models;

public class Product {
    private static int idGenerator = 0;
    private final Integer id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;

    private Product() {
        id = ++idGenerator;
    }

    public Product(String name, String description, double price) {
        this();
        setName(name);
        this.description = description;
        setPrice(price);
        this.stock = 0;
        this.imageUrl = null;
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
