package food_delivery_system.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static AuditService instance;
    private static final String FILE_PATH = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    /**
     * Logs an action to audit.csv.
     * Format: action_name,timestamp
     *
     * @param actionName the name of the action being performed
     */
    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String line = actionName + "," + timestamp;

        // append=true so each log call adds a new line without overwriting
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[Audit] Failed to write to audit.csv: " + e.getMessage());
        }
    }
}
