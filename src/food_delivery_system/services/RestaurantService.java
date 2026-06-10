package food_delivery_system.services;

import food_delivery_system.config.DatabaseHelper;
import food_delivery_system.models.Manager;
import food_delivery_system.models.Menu;
import food_delivery_system.models.Product;
import food_delivery_system.models.Restaurant;

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

        System.out.println("✅ Restaurantul '" + restaurant.getName() + "' a fost adăugat. (ID: " + generatedId + ")");
        AuditService.getInstance().logAction("ADD_RESTAURANT");
    }

    public Restaurant getRestaurantById(int id) {
        List<Restaurant> results = db.executeQuery(
            "SELECT * FROM restaurants WHERE id = ?",
            rs -> {
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
                // Load associated menus and products into the Restaurant object
                loadMenusForRestaurant(r);
                return r;
            }, id
        );
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Restaurant> getAllRestaurants() {
        return db.executeQuery(
            "SELECT * FROM restaurants",
            rs -> {
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
                return r;
            }
        );
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


    public void addMenuToRestaurant(int restaurantId, String menuName) {
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

    public void addProductToRestaurantMenu(int restaurantId, int menuId, String category, Product product) {
        if (product == null || category == null || category.isBlank())
            throw new IllegalArgumentException("Produsul și categoria sunt obligatorii!");

        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null)
            throw new IllegalArgumentException("Restaurantul cu ID-ul " + restaurantId + " nu a fost găsit!");

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
        AuditService.getInstance().logAction("ADD_PRODUCT_TO_MENU");
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
}
