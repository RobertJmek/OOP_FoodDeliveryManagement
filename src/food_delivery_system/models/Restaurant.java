package food_delivery_system.models;

import java.util.List;
import java.util.ArrayList;

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
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
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
        return menus;
    }
    public List<Manager> getManagers(){
        return managers;
    }
    public List<Review> getReviews(){
        return reviews;
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
