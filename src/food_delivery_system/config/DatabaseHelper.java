package food_delivery_system.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static DatabaseHelper instance;

    private DatabaseHelper() {}

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE statement.
     *
     * @param sql    the SQL string with '?' placeholders
     * @param params values to bind to the placeholders, in order
     * @return the generated key (for INSERTs) or the number of affected rows
     */
    public int executeUpdate(String sql, Object... params) {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();

            // Return the generated key if available (e.g. after an INSERT)
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            // For UPDATE / DELETE, return the number of affected rows
            return stmt.getUpdateCount();

        } catch (SQLException e) {
            throw new RuntimeException("executeUpdate failed: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a SELECT statement and maps each row to an object using the provided RowMapper.
     *
     * @param sql    the SQL string with '?' placeholders
     * @param mapper a RowMapper that converts a ResultSet row into an object of type T
     * @param params values to bind to the placeholders, in order
     * @return a list of mapped objects (empty list if no rows found)
     */
    public <T> List<T> executeQuery(String sql, RowMapper<T> mapper, Object... params) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        List<T> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("executeQuery failed: " + e.getMessage(), e);
        }

        return results;
    }
}
