package food_delivery_system.services;

import food_delivery_system.models.Restaurant;
import food_delivery_system.models.Menu;
import food_delivery_system.models.Product;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RestaurantService {
    private Map<Integer, Restaurant> allRestaurants;

    public RestaurantService() {
        this.allRestaurants = new HashMap<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurantul nu poate fi null!");
        }
        allRestaurants.put(restaurant.getId(), restaurant);
        System.out.println("✅ Restaurantul '" + restaurant.getName() + "' a fost adăugat în sistem.");
    }

    public Restaurant getRestaurantById(int id) {
        return allRestaurants.get(id);
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(allRestaurants.values());
    }

    public Menu getMenuFromRestaurantId(int restaurantId, int menuId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant != null) {
            for (Menu menu : restaurant.getMenus()) {
                if (menu.getId() == menuId) {
                    return menu;
                }
            }
        }
        return null;
    }

    public List<Menu> getAllMenusFromRestaurantId(int restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant != null) {
            return restaurant.getMenus();
        }
        return null;
    }

    public void addMenuToRestaurant(int restaurantId, String menu_name) {
        if (menu_name == null || menu_name.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele meniului nu poate fi null sau gol!");
        }
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Nu exista Restaurant cu id-ul " + restaurantId + "!");
        }
        restaurant.addMenu(menu_name);
        System.out.println( "📝 Meniul '" + menu_name + "' a fost adăugat la restaurantul " + restaurant.getName() + ".");
    }

    public void addProductToRestaurantMenu(int restaurantId, int menuId, String category, Product product) {

        if (product == null || category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Produsul și categoria sunt obligatorii!");
        }

        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("❌ Eroare: Restaurantul cu ID-ul " + restaurantId + " nu a fost găsit!");
        }

        restaurant.addProductToMenu(menuId, category, product);

    }
}
