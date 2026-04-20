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

    public void addProductToMenu(int menuId, String category, Product product) {
        if (product == null || category == null || category.isBlank()) {
            throw new IllegalArgumentException("Produsul și categoria sunt obligatorii!");
        }
        boolean menuFound = false;
        for (Menu menu : menus) {
            if (menu.getId() == menuId) {
                menu.addProduct(category, product);
                menuFound = true;
                break;
            }
        }
        if (!menuFound) {
            throw new IllegalArgumentException("❌ Eroare: Meniul cu ID-ul " + menuId + " nu aparține de restaurantul " + this.name);
        }
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
    public void setWebsite(String website) {
        if (website == null || !website.matches("^https?://.+")) {
            throw new IllegalArgumentException("Invalid website format!");
        }
        this.website = website;
    }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAddress(String address) { this.address = address; }
    public Menu addMenu(String menuName) {
        if (menuName == null || menuName.isBlank()) {
            throw new IllegalArgumentException("Numele meniului este obligatoriu!");
        }
        Menu newMenu = new Menu(menuName);
        menus.add(newMenu);

        return newMenu;
    }
    public void addManager(Manager manager){
        if (manager == null) {
            throw new IllegalArgumentException("Manager-ul nu poate fi null!");
        }
        managers.add(manager);
    }
    public void addReview(Review review){
        if (review == null) {
            throw new IllegalArgumentException("Review-ul nu poate fi null!");
        }
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
