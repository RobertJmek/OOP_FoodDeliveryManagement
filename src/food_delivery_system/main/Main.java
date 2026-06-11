package food_delivery_system.main;

import food_delivery_system.config.DatabaseConnection;
import food_delivery_system.models.*;
import food_delivery_system.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // Culori ANSI pentru un UI in terminal mai atragator
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    // Servicii — backed by MySQL via DatabaseHelper
    private static final FoodDeliveryService foodDeliveryService = new FoodDeliveryService();
    private static final UserService userService = foodDeliveryService.getUserService();
    private static final RestaurantService restaurantService = foodDeliveryService.getRestaurantService();
    private static final OrderService orderService = foodDeliveryService.getOrderService();

    public static void main(String[] args) {
        // Initialize DB connection and auto-create schema
        DatabaseConnection.getInstance();


        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Selectează o opțiune: ");

            try {
                switch (choice) {
                    case 0 -> running = false;
                    case 1 -> handleRegisterUser();
                    case 2 -> handleAddRestaurant();
                    case 3 -> handleAddProductToMenu();
                    case 4 -> handleQueryMenu();
                    case 5 -> handleSearchRestaurant();
                    case 6 -> handlePlaceOrder();
                    case 7 -> handleAvailableDrivers();
                    case 8 -> handleAssignDriver();
                    case 9 -> handleUpdateOrderStatus();
                    case 10 -> handleOrderHistory();
                    case 11 -> handleAddReview();
                    case 12 -> handleDisplayReviews();
                    case 13 -> handleAddMenu();
                    default -> System.out.println(RED + "❌ Opțiune invalidă! Încearcă din nou." + RESET);
                }
            } catch (Exception e) {
                System.out.println("\n" + RED + BOLD + "❌ EROARE: " + e.getMessage() + RESET);
            }
        }
        System.out.println("\n" + RED + BOLD + "=== SYSTEM SHUTDOWN ===" + RESET);
    }

    private static void printMenu() {
        System.out.println("\n" + CYAN + BOLD + "=======================================================" + RESET);
        System.out.println(YELLOW + BOLD + "   🍔🚚 FOOD DELIVERY MANAGEMENT SYSTEM - MAIN MENU" + RESET);
        System.out.println(CYAN + BOLD + "=======================================================" + RESET);
        System.out.println(GREEN + "  [1]" + RESET + " Înregistrare utilizator nou (Customer, Driver, Manager)");
        System.out.println(GREEN + "  [2]" + RESET + " Adăugare restaurant nou");
        System.out.println(GREEN + "  [3]" + RESET + " Adăugare produs în meniu (Manager)");
        System.out.println(GREEN + "  [4]" + RESET + " Interogare meniu restaurant");
        System.out.println(GREEN + "  [5]" + RESET + " Căutare restaurant (nume)");
        System.out.println(BLUE + "  [6]" + RESET + " Plasare comandă nouă");
        System.out.println(BLUE + "  [7]" + RESET + " Afișare șoferi disponibili");
        System.out.println(BLUE + "  [8]" + RESET + " Asignare șofer disponibil la comandă");
        System.out.println(BLUE + "  [9]" + RESET + " Actualizare status comandă");
        System.out.println(PURPLE + " [10]" + RESET + " Istoric comenzi client");
        System.out.println(PURPLE + " [11]" + RESET + " Adăugare recenzie local");
        System.out.println(PURPLE + " [12]" + RESET + " Afișare recenzii local");
        System.out.println(GREEN + " [13]" + RESET + " Adăugare meniu nou la restaurant (Manager)");
        System.out.println(RED + "  [0]" + RESET + " Ieșire din sistem");
        System.out.println(CYAN + BOLD + "=======================================================" + RESET);
    }

    // --- 1. ÎNREGISTRARE USER ---

    private static void handleRegisterUser() {
        System.out.println("\n--- Adăugare Utilizator Nou ---");
        System.out.println("Ce tip de utilizator înregistrăm? (1=Customer, 2=Driver, 3=Manager)");
        int type = readInt("Tip: ");

        String fName = readString("Prenume: ");
        String lName = readString("Nume Familie: ");
        String phone = readString("Telefon (10 cifre): ");
        String email = readString("Email: ");

        User user = null;
        if (type == 1) {
            user = new Customer(fName, lName, phone, email);
            String fAddress = readString("Adresă livrare implicită: ");
            ((Customer) user).addAddress(fAddress);
        } else if (type == 2) {
            user = new Driver(fName, lName, phone, email);
        } else if (type == 3) {
            user = new Manager(fName, lName, phone, email);
        } else {
            System.out.println("❌ Tip invalid.");
            return;
        }

        try {
            userService.registerUser(user);
        } catch (Exception e) {
            System.out.println("❌ Eroare la înregistrare: " + e.getMessage());
        }
    }

    // --- 2. ADĂUGARE RESTAURANT ---

    private static void handleAddRestaurant() {
        System.out.println("\n--- Adăugare Restaurant Nou ---");
        String name = readString("Nume Restaurant: ");
        String desc = readString("Descriere: ");
        String url = readString("URL Imagine (poate fi gol): ");
        String addr = readString("Adresă Restaurant: ");
        String phone = readString("Telefon: ");
        String email = readString("Email Contact: ");
        String web = readString("Website: ");

        Restaurant r = new Restaurant(name, desc, url, addr, phone, email, web);

        int managerId = readInt("Introdu ID-ul unui Manager existent pentru a-l asocia: ");
        User mgr = userService.getUserById(managerId);
        if (mgr instanceof Manager) {
            r.addManager((Manager) mgr);
        } else {
            System.out.println("⚠️ Utilizatorul cu ID " + managerId + " nu este un Manager valid! Trecem peste asociere.");
        }
        restaurantService.addRestaurant(r);
    }

    // --- 3. GESTIONARE MENIU (Adăugare Produs) ---
    private static void handleAddProductToMenu() {
        System.out.println("\n--- Adăugare Produs în Meniu ---");
        int rId = readInt("ID Restaurant: ");
        
        Restaurant r = restaurantService.getRestaurantById(rId);
        if (r == null) {
            System.out.println("❌ Restaurantul nu a fost găsit!");
            return;
        }

        int managerId = readInt("ID-ul tău de Manager: ");
        User mgr = userService.getUserById(managerId);
        if (!(mgr instanceof Manager) || !r.getManagers().contains(mgr)) {
            System.out.println("❌ Acțiune interzisă! Trebuie să fii manager al acestui local pentru a putea edita meniul.");
            return;
        }

        int mId = readInt("ID Meniu (ex. 1): ");

        String category = readString("Categorie produs (ex: Burgeri, Băuturi): ");
        String pName = readString("Nume Produs: ");
        String pDesc = readString("Descriere: ");
        double pPrice = readDouble("Preț (RON): ");

        Product p = new Product(pName, pDesc, pPrice);
        restaurantService.addProductToRestaurantMenu(rId, mId, category, p, (Manager) mgr);
    }

    // --- 13. ADĂUGARE MENIU NOU ---
    private static void handleAddMenu() {
        System.out.println("\n--- Adăugare Meniu Nou ---");
        int rId = readInt("ID Restaurant: ");

        Restaurant r = restaurantService.getRestaurantById(rId);
        if (r == null) {
            System.out.println("❌ Restaurantul nu a fost găsit!");
            return;
        }

        int managerId = readInt("ID-ul tău de Manager: ");
        User mgr = userService.getUserById(managerId);
        if (!(mgr instanceof Manager) || !r.getManagers().contains(mgr)) {
            System.out.println("❌ Acțiune interzisă! Trebuie să fii manager al acestui local pentru a putea edita meniul.");
            return;
        }

        String menuName = readString("Nume Meniu (ex: Meniu Principal): ");
        restaurantService.addMenuToRestaurant(rId, menuName, (Manager) mgr);
    }

    // --- 4. INTEROGARE MENIU ---
    private static void handleQueryMenu() {
        System.out.println("\n--- Interogare Meniu Restaurant ---");
        int rId = readInt("ID Restaurant: ");
        Restaurant r = restaurantService.getRestaurantById(rId);
        if (r == null) {
            System.out.println("❌ Restaurantul nu a fost găsit.");
            return;
        }
        System.out.println("\n[ Meniuri la " + r.getName() + " ]");
        for (Menu m : r.getMenus()) {
            System.out.println("Meniu #" + m.getId() + ": " + m.getName());
            for (String category : m.getCategories()) {
                System.out.println("  >> Categoria: " + category);
                List<Product> prods = new ArrayList<>(m.getProducts(category));
                prods.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                for (Product p : prods) {
                    System.out.println("     - " + p.getName() + " | " + p.getPrice() + " RON | " + p.getDescription());
                }
            }
        }
        AuditService.getInstance().logAction("QUERY_MENU", null, "restaurant '" + r.getName() + "' (ID " + rId + ")");
    }

    // --- 5. CĂUTARE RESTAURANT ---
    private static void handleSearchRestaurant() {
        System.out.println("\n--- Căutare Restaurant ---");
        String keyword = readString("Introdu cuvânt cheie din nume: ").toLowerCase();

        List<Restaurant> found = restaurantService.getAllRestaurants().stream()
                .filter(r -> r.getName().toLowerCase().contains(keyword))
                .toList();

        if(found.isEmpty()) {
            System.out.println("Nu s-au găsit restaurante.");
        } else {
            for(Restaurant r : found) {
                System.out.println(" - ID " + r.getId() + " | " + r.getName() + " | " + r.getAddress());
            }
        }
        AuditService.getInstance().logAction("SEARCH_RESTAURANT", null, "keyword='" + keyword + "', results=" + found.size());
    }

    // --- 6. PLASARE COMANDĂ ---
    private static void handlePlaceOrder() {
        System.out.println("\n--- Plasare Comandă ---");
        int cId = readInt("ID Customer: ");
        User user = userService.getUserById(cId);
        if (!(user instanceof Customer)) {
            System.out.println("❌ ID invalid sau nu este de tip Customer!");
            return;
        }

        int rId = readInt("ID Restaurant: ");
        Restaurant r = restaurantService.getRestaurantById(rId);
        if (r == null) { System.out.println("❌ Restaurant negăsit."); return; }

        System.out.println("Introdu ID-uri de produse. Tastează 0 pentru a finaliza.");
        List<Product> orderedProducts = new ArrayList<>();

        while (true) {
            int pId = readInt("ID Produs (0 = Gata): ");
            if (pId == 0) break;

            // Căutare produs în restaurantul respectiv
            Product foundProd = null;
            for(Menu m : r.getMenus()) {
                for(String cat : m.getCategories()) {
                    for(Product p : m.getProducts(cat)) {
                        if (p.getId().equals(pId)) {
                            foundProd = p;
                            break;
                        }
                    }
                }
            }
            if (foundProd != null) {
                orderedProducts.add(foundProd);
                System.out.println("   + Adăugat " + foundProd.getName());
            } else {
                System.out.println("   ! Produsul nu există în acest restaurant.");
            }
        }

        if (orderedProducts.isEmpty()) {
            System.out.println("❌ Comanda nu poate fi goală!");
            return;
        }

        double deliveryFee = 15.0; // Taxă fixă test
        orderService.placeOrder((Customer) user, r, orderedProducts, deliveryFee);
    }

    // --- 7. ȘOFERI DISPONIBILI ---
    private static void handleAvailableDrivers() {
        System.out.println("\n--- Șoferi Disponibili ---");

        // Colectăm șoferii ocupați
        List<Driver> busyDrivers = orderService.getAllOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.OUT_FOR_DELIVERY && o.getDriver() != null)
                .map(Order::getDriver)
                .toList();

        // Șoferii disponibili: marcați ca disponibili în baza de date ȘI care nu sunt deja ocupați
        List<Driver> drivers = userService.getAllUsers().stream()
                .filter(u -> u instanceof Driver)
                .map(u -> (Driver) u)
                .filter(Driver::isAvailable)
                .filter(d -> !busyDrivers.contains(d))
                .toList();

        if(drivers.isEmpty()) {
            System.out.println("Niciun șofer disponibil momentan.");
        } else {
            for(Driver d : drivers) {
                System.out.println(" - " + d.getFullName() + " (ID: " + d.getId() + ")");
            }
        }
        AuditService.getInstance().logAction("QUERY_AVAILABLE_DRIVERS", null, "results=" + drivers.size());
    }

    // --- 8. ASIGNARE ȘOFER ---
    private static void handleAssignDriver() {
        System.out.println("\n--- Asignare Șofer ---");
        int oId = readInt("ID Comandă: ");
        int dId = readInt("ID Driver: ");

        User user = userService.getUserById(dId);
        if(user instanceof Driver) {
            orderService.assignDriverToOrder(oId, (Driver) user);
        } else {
            System.out.println("❌ Utilizatorul nu există sau nu e șofer.");
        }
    }

    // --- 9. ACTUALIZARE STATUS ---
    private static void handleUpdateOrderStatus() {
        System.out.println("\n--- Schimbare Stare Comandă ---");
        int oId = readInt("ID Comandă: ");
        System.out.println("Statusuri: 1=PENDING, 2=PREPARING, 3=READY_FOR_PICKUP, 4=OUT_FOR_DELIVERY, 5=DELIVERED, 6=CANCELED");
        int s = readInt("Alege noul status: ");
        OrderStatus nStatus;
        switch(s) {
            case 1 -> nStatus = OrderStatus.PENDING;
            case 2 -> nStatus = OrderStatus.PREPARING;
            case 3 -> nStatus = OrderStatus.READY_FOR_PICKUP;
            case 4 -> nStatus = OrderStatus.OUT_FOR_DELIVERY;
            case 5 -> nStatus = OrderStatus.DELIVERED;
            case 6 -> nStatus = OrderStatus.CANCELED;
            default -> { System.out.println("❌ Invalid."); return; }
        }
        orderService.updateOrderStatus(oId, nStatus);
    }

    // --- 10. ISTORIC COMENZI ---
    private static void handleOrderHistory() {
        System.out.println("\n--- Istoric Comenzi Client ---");
        int cId = readInt("ID Customer: ");
        List<Order> istoric = orderService.getOrderHistoryForCustomer(cId);

        if (istoric.isEmpty()) {
            System.out.println("Clientul nu are comenzi plasate.");
        } else {
            for(Order o : istoric) {
                System.out.println(" - Comanda #" + o.getId() + " | Local: " + o.getRestaurant().getName() + " | Status: " + o.getStatus() + " | Total: " + o.calculateTotalToPay() + " RON");
            }
        }
        AuditService.getInstance().logAction("QUERY_ORDER_HISTORY", null, "customer ID " + cId + ", orders=" + istoric.size());
    }

    // --- 11. ADĂUGARE RECENZIE ---
    private static void handleAddReview() {
        System.out.println("\n--- Adăugare Recenzie ---");
        int cId = readInt("ID Customer: ");
        User user = userService.getUserById(cId);
        if(!(user instanceof Customer)) {
             System.out.println("❌ ID invalid de client!"); return;
        }

        int rId = readInt("ID Restaurant evaluat: ");
        Restaurant r = restaurantService.getRestaurantById(rId);
        if(r == null) { System.out.println("❌ Restaurant negăsit!"); return; }

        int rating = readInt("Rating (1-5): ");
        String comment = readString("Text recenzie: ");

        Review rev = new Review((Customer) user, comment, rating);
        restaurantService.addReviewToRestaurant(rId, rev);
        System.out.println("✅ Recenzia a fost salvată cu succes!");
    }

    // --- 12. AFIȘARE RECENZII ---
    private static void handleDisplayReviews() {
        System.out.println("\n--- Recenziile unui Local ---");
        int rId = readInt("ID Restaurant: ");
        Restaurant r = restaurantService.getRestaurantById(rId);
        if (r == null) { System.out.println("❌ Restaurant negăsit."); return; }

        List<Review> revs = r.getReviews();
        if(revs.isEmpty()) {
            System.out.println("Nu există recenzii pentru " + r.getName() + ".");
        } else {
            System.out.println("Scor general orientativ: " + r.getName());
            for(Review rev : revs) {
                System.out.println(" ⭐ " + rev.getRating() + "/5 | " + rev.getCustomer().getFullName() + " a scris: " + rev.getComment());
            }
        }
        AuditService.getInstance().logAction("DISPLAY_REVIEWS", null, "restaurant '" + r.getName() + "' (ID " + rId + "), reviews=" + revs.size());
    }

    // ==========================================
    // UTILS & DUMMY DATA
    // ==========================================

    private static int readInt(String promt) {
        System.out.print(YELLOW + BOLD + " ❯ " + RESET + promt);
        while (!scanner.hasNextInt()) {
            System.out.println(RED + "❌ Te rog introdu un număr valid!" + RESET);
            scanner.next();
            System.out.print(YELLOW + BOLD + " ❯ " + RESET + promt);
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        return val;
    }

    private static double readDouble(String promt) {
        System.out.print(YELLOW + BOLD + " ❯ " + RESET + promt);
        while (!scanner.hasNextDouble()) {
            System.out.println(RED + "❌ Te rog introdu un preț valid!" + RESET);
            scanner.next();
            System.out.print(YELLOW + BOLD + " ❯ " + RESET + promt);
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }

    private static String readString(String promt) {
        System.out.print(CYAN + BOLD + " ❯ " + RESET + promt);
        return scanner.nextLine().trim();
    }

}
