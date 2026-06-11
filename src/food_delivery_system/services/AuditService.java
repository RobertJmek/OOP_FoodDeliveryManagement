package food_delivery_system.services;

import food_delivery_system.models.Customer;
import food_delivery_system.models.Driver;
import food_delivery_system.models.Manager;
import food_delivery_system.models.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static AuditService instance;
    private static final String FILE_PATH = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String HEADER = "timestamp,action,actor_role,actor_id,actor_name,details";

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // logs an action with no associated user
    public void logAction(String actionName) {
        logAction(actionName, null, null);
    }

    // logs an action performed by a given user
    public void logAction(String actionName, User actor) {
        logAction(actionName, actor, null);
    }

    // Logs an action to audit.csv with full context. Columns: timestamp, action, actor_role, actor_id, actor_name, details

    public void logAction(String actionName, User actor, String details) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String actorId   = (actor == null) ? "-" : String.valueOf(actor.getId());
        String actorName = (actor == null) ? "-" : actor.getFullName();

        String line = String.join(",",
            csv(timestamp),
            csv(actionName),
            csv(roleOf(actor)),
            csv(actorId),
            csv(actorName),
            csv(details == null ? "" : details)
        );

        // Write a header row the first time the file is created.
        boolean writeHeader = !new File(FILE_PATH).exists();

        // append=true so each log call adds a new line without overwriting
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (writeHeader) {
                writer.write(HEADER);
                writer.newLine();
            }
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[Audit] Failed to write to audit.csv: " + e.getMessage());
        }
    }

    private String roleOf(User u) {
        if (u == null)             return "SYSTEM";
        if (u instanceof Manager)  return "MANAGER";
        if (u instanceof Driver)   return "DRIVER";
        if (u instanceof Customer) return "CUSTOMER";
        return "USER";
    }

    // Wraps a value as a quoted CSV field, escaping embedded quotes so commas are safe.

    private String csv(String value) {
        if (value == null) value = "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
