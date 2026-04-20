package food_delivery_system.models;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Menu {
    private Map<String, List<Product>> products;
    private static int idGenerator = 0;
    private final int id;
    private String name;

    Menu(String name) {
        id = ++idGenerator;
        products = new HashMap<>();
        this.name = name;
    }

    public void addProduct(String category, Product product) {
        products.putIfAbsent(category, new ArrayList<>());
        products.get(category).add(product);
    }

    public List<Product> getProducts(String category) {
        return Collections.unmodifiableList(products.getOrDefault(category, new ArrayList<>()));
    }

    public List<String> getCategories() {
        return new ArrayList<>(products.keySet());
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

}
