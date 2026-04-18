package food_delivery_system.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Restaurant {
    private static int idGenerator = 0;
    private final int id;
    private String name;
    private String description;
    private String imageUrl;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;

    private List<Menu> menus;
    private List<Manager> managers;
    private List<Review> reviews;

    private Restaurant() {
        id = ++idGenerator;
        menus = new ArrayList<>();
        managers = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public Restaurant(String name, String description, String imageUrl, String address, String phoneNumber, String email, String website) {
        this();
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.address = address;
        setPhoneNumber(phoneNumber);
        setEmail(email);
        this.website = website;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Invalid phone number! It must contain exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format!");
        }
        this.email = email;
    }

    public void addMenu(Menu menu){
        menus.add(menu);
    }
    public void addManager(Manager manager){
        managers.add(manager);
    }
    public void addReview(Review review){
        reviews.add(review);
    }
    public List<Menu> getMenus(){
        return Collections.unmodifiableList(menus);
    }
    public List<Manager> getManagers(){
        return Collections.unmodifiableList(managers);
    }
    public List<Review> getReviews(){
        return Collections.unmodifiableList(reviews);
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getWebsite() { return website; }
    public Integer getId() { return id; }


}
