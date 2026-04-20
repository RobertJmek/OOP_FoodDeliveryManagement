package food_delivery_system.services;

import food_delivery_system.models.User;
import food_delivery_system.models.Driver;
import food_delivery_system.models.Manager;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class UserService {
    private Map<Integer, User> allUsers;

    private Set<String> registeredEmails;

    public UserService() {
        this.allUsers = new HashMap<>();
        this.registeredEmails = new HashSet<>();
    }

    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("❌ Utilizatorul nu poate fi null!");
        }

        String userEmail = user.getEmail().toLowerCase();
        if (registeredEmails.contains(userEmail)) {
            throw new IllegalArgumentException(
                    "❌ Eroare la înregistrare: Un cont cu adresa de e-mail '" + user.getEmail() + "' există deja!");
        }

        // Dacă e-mail-ul e liber, salvăm utilizatorul în structurile noastre.
        allUsers.put(user.getId(), user);
        registeredEmails.add(userEmail);

        System.out.println(
                "✅ " + user.getClass().getSimpleName() + " '" + user.getFullName() + "' a fost înregistrat cu succes!");
    }

    // 2. Găsire utilizator după ID în timp fix O(1).

    public User getUserById(int id) {
        return allUsers.get(id); // Poate returna null dacă ID-ul nu e găsit
    }

    // 3. Găsire utilizator după adresa de e-mail

    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mailul căutat nu poate fi null sau vid!");
        }
        for (User user : allUsers.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;}
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(allUsers.values());
    }

    public List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();
        for ( User user : allUsers.values() ) {
            if (user instanceof Manager manager) {
                managers.add(manager);
            }
        }
        return managers;
    }

    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        for ( User user : allUsers.values() ) {
            if (user instanceof Driver driver) {
                drivers.add(driver);
            }
        }
        return drivers;
    }

    public List<Driver> getAllAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for ( User user : allUsers.values() ) {
            if (user instanceof Driver driver && driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    public Driver getBestAvailableDriver() {
        List<Driver> availableDrivers = getAllAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            return null;
        }
        return availableDrivers.get(0);
    }
}
