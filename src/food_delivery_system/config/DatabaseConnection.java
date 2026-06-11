package food_delivery_system.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    // from .env file
    private static final Map<String, String> ENV = loadEnvFile();

    private static final String HOST     = getEnv("DB_HOST",     "localhost");
    private static final String PORT     = getEnv("DB_PORT",     "3306");
    private static final String DATABASE = getEnv("DB_NAME",     "");
    private static final String USER     = getEnv("DB_USER",     "");
    private static final String PASSWORD = getEnv("DB_PASSWORD", "");

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] Connected to database: " + DATABASE);
            initSchema();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[DB] MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new RuntimeException("[DB] Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }


    private void initSchema() throws SQLException {
        // users

        String[] tables = {
            """
            CREATE TABLE IF NOT EXISTS users (
                id         INT AUTO_INCREMENT PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                last_name  VARCHAR(100) NOT NULL,
                phone      VARCHAR(10)  NOT NULL,
                email      VARCHAR(255) NOT NULL UNIQUE,
                type       ENUM('CUSTOMER', 'DRIVER', 'MANAGER') NOT NULL
            )
            """,

            // customers
            """
            CREATE TABLE IF NOT EXISTS customers (
                user_id         INT PRIMARY KEY,
                default_address VARCHAR(500),
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """,

            // drivers
            """
            CREATE TABLE IF NOT EXISTS drivers (
                user_id      INT PRIMARY KEY,
                vehicle_type VARCHAR(100),
                is_available BOOLEAN NOT NULL DEFAULT FALSE,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """,

            // restaurants
            """
            CREATE TABLE IF NOT EXISTS restaurants (
                id          INT AUTO_INCREMENT PRIMARY KEY,
                name        VARCHAR(255) NOT NULL,
                description TEXT,
                image_url   VARCHAR(500),
                address     VARCHAR(500),
                phone       VARCHAR(10),
                email       VARCHAR(255),
                website     VARCHAR(500)
            )
            """,

            // restaurant_managers (many-to-many: which managers administer which restaurant)
            """
            CREATE TABLE IF NOT EXISTS restaurant_managers (
                restaurant_id INT NOT NULL,
                manager_id    INT NOT NULL,
                PRIMARY KEY (restaurant_id, manager_id),
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
                FOREIGN KEY (manager_id)    REFERENCES users(id)       ON DELETE CASCADE
            )
            """,

            // menus
            """
            CREATE TABLE IF NOT EXISTS menus (
                id            INT AUTO_INCREMENT PRIMARY KEY,
                restaurant_id INT NOT NULL,
                name          VARCHAR(255) NOT NULL,
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
            )
            """,

            // products
            """
            CREATE TABLE IF NOT EXISTS products (
                id          INT AUTO_INCREMENT PRIMARY KEY,
                name        VARCHAR(255) NOT NULL,
                description TEXT,
                price       DECIMAL(10, 2) NOT NULL
            )
            """,

            // menu_products
            """
            CREATE TABLE IF NOT EXISTS menu_products (
                menu_id       INT NOT NULL,
                category_name VARCHAR(100) NOT NULL,
                product_id    INT NOT NULL,
                FOREIGN KEY (menu_id)    REFERENCES menus(id)    ON DELETE CASCADE,
                FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
            )
            """,

            // orders
            """
            CREATE TABLE IF NOT EXISTS orders (
                id            INT AUTO_INCREMENT PRIMARY KEY,
                customer_id   INT NOT NULL,
                restaurant_id INT NOT NULL,
                driver_id     INT,
                status        VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                delivery_fee  DECIMAL(10, 2) NOT NULL DEFAULT 0,
                total_price   DECIMAL(10, 2) NOT NULL DEFAULT 0,
                discount      DECIMAL(5, 2)  NOT NULL DEFAULT 0,
                FOREIGN KEY (customer_id)   REFERENCES users(id)        ON DELETE RESTRICT,
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)  ON DELETE RESTRICT,
                FOREIGN KEY (driver_id)     REFERENCES users(id)        ON DELETE SET NULL
            )
            """,

            // order-items
            """
            CREATE TABLE IF NOT EXISTS order_items (
                order_id   INT NOT NULL,
                product_id INT NOT NULL,
                FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
                FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
            )
            """,

            // reviews

            """
            CREATE TABLE IF NOT EXISTS reviews (
                id            INT AUTO_INCREMENT PRIMARY KEY,
                customer_id   INT NOT NULL,
                restaurant_id INT NOT NULL,
                comment       TEXT,
                rating        INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
                FOREIGN KEY (customer_id)   REFERENCES users(id)       ON DELETE CASCADE,
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
            )
            """
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : tables) {
                stmt.execute(sql);
            }
        }
        System.out.println("[DB] Schema initialized successfully.");
    }

    // .env map first, then OS env vars, then falls back to default
    private static String getEnv(String key, String defaultValue) {
        String value = ENV.getOrDefault(key, System.getenv(key));
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    // parses .env file from the project root directory
    private static Map<String, String> loadEnvFile() {
        Map<String, String> map = new HashMap<>();
        String envPath = System.getProperty("user.dir") + "/.env";

        try (BufferedReader reader = new BufferedReader(new FileReader(envPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) continue;

                int eq = line.indexOf('=');
                if (eq > 0) {
                    String key   = line.substring(0, eq).trim();
                    String value = line.substring(eq + 1).trim();
                    map.put(key, value);
                }
            }
            System.out.println("[DB] Loaded .env from: " + envPath);
        } catch (IOException e) {
            System.out.println("[DB] No .env file found at " + envPath + " — using defaults.");
        }
        return map;
    }
}
