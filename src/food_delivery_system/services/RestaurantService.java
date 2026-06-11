package food_delivery_system.services;

import food_delivery_system.config.DatabaseHelper;
import food_delivery_system.models.Customer;
import food_delivery_system.models.Manager;
import food_delivery_system.models.Menu;
import food_delivery_system.models.Product;
import food_delivery_system.models.Restaurant;
import food_delivery_system.models.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RestaurantService {

    private final DatabaseHelper db = DatabaseHelper.getInstance();

    // -------------------------------------------------------------------------
    // RESTAURANT CRUD
    // -------------------------------------------------------------------------

    public void addRestaurant(Restaurant restaurant) {
        if (restaurant == null) throw new IllegalArgumentException("Restaurantul nu poate fi null!");

        int generatedId = db.executeUpdate(
            "INSERT INTO restaurants (name, description, image_url, address, phone, email, website) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            restaurant.getName(), restaurant.getDescription(), restaurant.getImageUrl(),
            restaurant.getAddress(), restaurant.getPhoneNumber(), restaurant.getEmail(), restaurant.getWebsite()
        );
        restaurant.setId(generatedId);

        // Persist the manager associations created in memory before saving
        for (Manager manager : restaurant.getManagers()) {
            db.executeUpdate(
                "INSERT INTO restaurant_managers (restaurant_id, manager_id) VALUES (?, ?)",
                generatedId, manager.getId()
            );
        }

        System.out.println("✅ Restaurantul '" + restaurant.getName() + "' a fost adăugat. (ID: " + generatedId + ")");
        Manager actor = restaurant.getManagers().isEmpty() ? null : restaurant.getManagers().get(0);
        AuditService.getInstance().logAction("ADD_RESTAURANT", actor,
            "restaurant='" + restaurant.getName() + "' (ID " + generatedId + ")");
    }

    public Restaurant getRestaurantById(int id) {
        List<Restaurant> results = db.executeQuery(
            "SELECT * FROM restaurants WHERE id = ?", this::mapRestaurantRow, id
        );
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Restaurant> getAllRestaurants() {
        return db.executeQuery("SELECT * FROM restaurants", this::mapRestaurantRow);
    }

    // Builds a Restaurant from a row and eagerly loads its menus, managers and reviews.
    private Restaurant mapRestaurantRow(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("image_url"),
            rs.getString("address"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("website")
        );
        loadMenusForRestaurant(r);
        loadManagersForRestaurant(r);
        loadReviewsForRestaurant(r);
        return r;
    }

    public void updateRestaurant(Restaurant restaurant) {
        db.executeUpdate(
            "UPDATE restaurants SET name=?, description=?, image_url=?, address=?, phone=?, email=?, website=? WHERE id=?",
            restaurant.getName(), restaurant.getDescription(), restaurant.getImageUrl(),
            restaurant.getAddress(), restaurant.getPhoneNumber(), restaurant.getEmail(),
            restaurant.getWebsite(), restaurant.getId()
        );
        System.out.println("✅ Restaurantul ID " + restaurant.getId() + " a fost actualizat.");
    }

    public void deleteRestaurant(int id) {
        db.executeUpdate("DELETE FROM restaurants WHERE id=?", id);
        System.out.println("✅ Restaurantul ID " + id + " a fost șters.");
    }


    // MENU OPERATIONS


    public void addMenuToRestaurant(int restaurantId, String menuName, Manager actor) {
        if (menuName == null || menuName.isBlank())
            throw new IllegalArgumentException("Numele meniului nu poate fi null sau gol!");

        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null)
            throw new IllegalArgumentException("Nu există restaurant cu ID-ul " + restaurantId);

        int menuId = db.executeUpdate(
            "INSERT INTO menus (restaurant_id, name) VALUES (?, ?)",
            restaurantId, menuName
        );

        Menu menu = restaurant.addMenu(menuName);
        System.out.println("📝 Meniul '" + menuName + "' adăugat la " + restaurant.getName() + ". (ID: " + menuId + ")");
        AuditService.getInstance().logAction("ADD_MENU", actor,
            "menu='" + menuName + "' (ID " + menuId + ") la restaurant '" + restaurant.getName() + "' (ID " + restaurantId + ")");
    }

    public Menu getMenuFromRestaurantId(int restaurantId, int menuId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null) return null;
        for (Menu m : restaurant.getMenus()) {
            if (m.getId() == menuId) return m;
        }
        return null;
    }

    public List<Menu> getAllMenusFromRestaurantId(int restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        return restaurant == null ? null : restaurant.getMenus();
    }

    // PRODUCT CRUD

    public void addProductToRestaurantMenu(int restaurantId, int menuId, String category, Product product, Manager actor) {
        if (product == null || category == null || category.isBlank())
            throw new IllegalArgumentException("Produsul și categoria sunt obligatorii!");

        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null)
            throw new IllegalArgumentException("Restaurantul cu ID-ul " + restaurantId + " nu a fost găsit!");

        // Validate the menu belongs to this restaurant BEFORE inserting the product

        boolean menuBelongsToRestaurant = restaurant.getMenus().stream()
            .anyMatch(m -> m.getId() == menuId);
        if (!menuBelongsToRestaurant)
            throw new IllegalArgumentException(
                "Meniul cu ID-ul " + menuId + " nu aparține restaurantului " + restaurant.getName() + "!");

        int productId = db.executeUpdate(
            "INSERT INTO products (name, description, price) VALUES (?, ?, ?)",
            product.getName(), product.getDescription(), product.getPrice()
        );
        product.setId(productId);

        db.executeUpdate(
            "INSERT INTO menu_products (menu_id, category_name, product_id) VALUES (?, ?, ?)",
            menuId, category, productId
        );

        restaurant.addProductToMenu(menuId, category, product);

        System.out.println("✅ Produs '" + product.getName() + "' adăugat în meniu. (ID: " + productId + ")");
        AuditService.getInstance().logAction("ADD_PRODUCT_TO_MENU", actor,
            "product='" + product.getName() + "' (ID " + productId + ") in menu ID " + menuId
                + ", restaurant '" + restaurant.getName() + "' (ID " + restaurantId + ")");
    }

    public Product getProductById(int productId) {
        List<Product> results = db.executeQuery(
            "SELECT * FROM products WHERE id = ?",
            rs -> new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price")
            ), productId
        );
        return results.isEmpty() ? null : results.get(0);
    }

    public void updateProduct(Product product) {
        db.executeUpdate(
            "UPDATE products SET name=?, description=?, price=? WHERE id=?",
            product.getName(), product.getDescription(), product.getPrice(), product.getId()
        );
        System.out.println("✅ Produsul ID " + product.getId() + " a fost actualizat.");
    }

    public void deleteProduct(int productId) {
        db.executeUpdate("DELETE FROM products WHERE id=?", productId);
        System.out.println("✅ Produsul ID " + productId + " a fost șters.");
    }

    // REVIEW OPERATIONS

    public void addReviewToRestaurant(int restaurantId, Review review) {
        if (review == null) throw new IllegalArgumentException("Recenzia nu poate fi null!");

        int reviewId = db.executeUpdate(
            "INSERT INTO reviews (customer_id, restaurant_id, comment, rating) VALUES (?, ?, ?, ?)",
            review.getCustomer().getId(), restaurantId, review.getComment(), review.getRating()
        );
        review.setId(reviewId);
        AuditService.getInstance().logAction("ADD_REVIEW", review.getCustomer(),
            "restaurant ID " + restaurantId + ", rating " + review.getRating() + "/5");
    }

    // Helper — loads menus + products from DB into a Restaurant object

    private void loadMenusForRestaurant(Restaurant restaurant) {
        // Load menus
        List<Menu> menus = db.executeQuery(
            "SELECT * FROM menus WHERE restaurant_id = ?",
            rs -> new Menu(rs.getInt("id"), rs.getString("name")),
            restaurant.getId()
        );

        for (Menu menu : menus) {
            restaurant.addLoadedMenu(menu);

            // Load products for each menu with their category
            db.executeQuery(
                "SELECT p.*, mp.category_name FROM products p " +
                "JOIN menu_products mp ON p.id = mp.product_id " +
                "WHERE mp.menu_id = ?",
                rs -> {
                    Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                    );
                    menu.addProduct(rs.getString("category_name"), p);
                    return p;
                }, menu.getId()
            );
        }
    }

    // Helper — loads the managers associated with a restaurant
    private void loadManagersForRestaurant(Restaurant restaurant) {
        List<Manager> managers = db.executeQuery(
            "SELECT u.* FROM users u " +
            "JOIN restaurant_managers rm ON u.id = rm.manager_id " +
            "WHERE rm.restaurant_id = ?",
            rs -> new Manager(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email")
            ),
            restaurant.getId()
        );
        for (Manager manager : managers) {
            restaurant.addManager(manager);
        }
    }

    // Helper — loads the reviews (with their authoring customer) for a restaurant
    private void loadReviewsForRestaurant(Restaurant restaurant) {
        List<Review> reviews = db.executeQuery(
            "SELECT r.id AS review_id, r.comment, r.rating, " +
            "u.id AS customer_id, u.first_name, u.last_name, u.phone, u.email " +
            "FROM reviews r JOIN users u ON r.customer_id = u.id " +
            "WHERE r.restaurant_id = ?",
            rs -> new Review(
                rs.getInt("review_id"),
                new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email")
                ),
                rs.getString("comment"),
                rs.getInt("rating")
            ),
            restaurant.getId()
        );
        for (Review review : reviews) {
            restaurant.addReview(review);
        }
    }
}
