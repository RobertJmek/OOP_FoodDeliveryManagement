package food_delivery_system.services;

import food_delivery_system.config.DatabaseHelper;
import food_delivery_system.models.Customer;
import food_delivery_system.models.Driver;
import food_delivery_system.models.Manager;
import food_delivery_system.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final DatabaseHelper db = DatabaseHelper.getInstance();

    // CREATE

    public void registerUser(User user) {
        if (user == null) throw new IllegalArgumentException("Utilizatorul nu poate fi null!");

        // Check for duplicate email
        if (getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException(
                "Un cont cu adresa de e-mail '" + user.getEmail() + "' există deja!");
        }


        String type;
        if (user instanceof Customer) type = "CUSTOMER";
        else if (user instanceof Driver) type = "DRIVER";
        else if (user instanceof Manager) type = "MANAGER";
        else throw new IllegalArgumentException("Tip de utilizator necunoscut!");


        int generatedId = db.executeUpdate(
            "INSERT INTO users (first_name, last_name, phone, email, type) VALUES (?, ?, ?, ?, ?)",
            user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(), type
        );
        user.setId(generatedId);


        if (user instanceof Customer c) {
            String defaultAddress = c.getAddresses().isEmpty() ? null : c.getPreferredAddress();
            db.executeUpdate(
                "INSERT INTO customers (user_id, default_address) VALUES (?, ?)",
                generatedId, defaultAddress
            );
        } else if (user instanceof Driver d) {
            db.executeUpdate(
                "INSERT INTO drivers (user_id, vehicle_type, is_available) VALUES (?, ?, ?)",
                generatedId, d.getVehicleType(), d.isAvailable()
            );
        }

        System.out.println("✅ " + type + " '" + user.getFullName() + "' înregistrat cu succes! (ID: " + generatedId + ")");
        AuditService.getInstance().logAction("REGISTER_USER", user, "type=" + type + ", email=" + user.getEmail());
    }

    // READ

   public User getUserById(int id) {
        List<User> results = db.executeQuery(
            "SELECT u.*, c.default_address, d.vehicle_type, d.is_available " +
            "FROM users u " +
            "LEFT JOIN customers c ON u.id = c.user_id " +
            "LEFT JOIN drivers   d ON u.id = d.user_id " +
            "WHERE u.id = ?",
                this::mapRowToUser, id
        );
        return results.isEmpty() ? null : results.get(0);
    }

    public User getUserByEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email-ul nu poate fi null!");

        List<User> results = db.executeQuery(
            "SELECT u.*, c.default_address, d.vehicle_type, d.is_available " +
            "FROM users u " +
            "LEFT JOIN customers c ON u.id = c.user_id " +
            "LEFT JOIN drivers   d ON u.id = d.user_id " +
            "WHERE u.email = ?",
            rs -> mapRowToUser(rs), email
        );
        return results.isEmpty() ? null : results.get(0);
    }

    public List<User> getAllUsers() {
        return db.executeQuery(
            "SELECT u.*, c.default_address, d.vehicle_type, d.is_available " +
            "FROM users u " +
            "LEFT JOIN customers c ON u.id = c.user_id " +
            "LEFT JOIN drivers   d ON u.id = d.user_id",
            rs -> mapRowToUser(rs)
        );
    }

    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        for (User u : getAllUsers()) {
            if (u instanceof Driver d) drivers.add(d);
        }
        return drivers;
    }

    public List<Driver> getAllAvailableDrivers() {
        List<Driver> available = new ArrayList<>();
        for (Driver d : getAllDrivers()) {
            if (d.isAvailable()) available.add(d);
        }
        return available;
    }

    // UPDATE

    public void updateUser(User user) {
        db.executeUpdate(
            "UPDATE users SET first_name=?, last_name=?, phone=?, email=? WHERE id=?",
            user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(), user.getId()
        );

        if (user instanceof Driver d) {
            db.executeUpdate(
                "UPDATE drivers SET vehicle_type=?, is_available=? WHERE user_id=?",
                d.getVehicleType(), d.isAvailable(), d.getId()
            );
        } else if (user instanceof Customer c) {
            db.executeUpdate(
                "UPDATE customers SET default_address=? WHERE user_id=?",
                c.getPreferredAddress(), c.getId()
            );
        }

        System.out.println("✅ Utilizatorul ID " + user.getId() + " a fost actualizat.");
    }

    // DELETE

    public void deleteUser(int id) {
        // Cascades to customers / drivers tables via FK ON DELETE CASCADE
        db.executeUpdate("DELETE FROM users WHERE id=?", id);
        System.out.println("✅ Utilizatorul ID " + id + " a fost șters.");
    }


    private User mapRowToUser(java.sql.ResultSet rs) throws java.sql.SQLException {
        int    id        = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName  = rs.getString("last_name");
        String phone     = rs.getString("phone");
        String email     = rs.getString("email");
        String type      = rs.getString("type");

        return switch (type) {
            case "CUSTOMER" -> {
                Customer c = new Customer(id, firstName, lastName, phone, email);
                String addr = rs.getString("default_address");
                if (addr != null && !addr.isBlank()) c.addAddress(addr);
                yield c;
            }
            case "DRIVER" -> {
                Driver d = new Driver(id, firstName, lastName, phone, email);
                d.setVehicleType(rs.getString("vehicle_type"));
                d.setAvailable(rs.getBoolean("is_available"));
                yield d;
            }
            case "MANAGER" -> new Manager(id, firstName, lastName, phone, email);
            default -> throw new IllegalArgumentException("Tip necunoscut: " + type);
        };
    }
}
