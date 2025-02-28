package app.controllers;

import app.models.BabyTrackingEntry;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    private Connection connection;

    public void initialize() {
        // Læs .env-filen korrekt
        Dotenv dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir")) // Finder .env i projektets rodmappe
                .ignoreIfMissing()
                .load();

        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        // Debug: Udskriv værdierne
        System.out.println("DEBUG: DB_URL = " + url);
        System.out.println("DEBUG: DB_USER = " + user);
        System.out.println("DEBUG: DB_PASSWORD = " + (password != null ? "********" : "null")); // Maskér password

        if (url == null || user == null || password == null) {
            throw new IllegalStateException("❌ Mangler databaseoplysninger i .env-filen!");
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to the database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // ✅ Metode til at hente alle baby-tracking entries
    public List<BabyTrackingEntry> getAllBabyEntries() {
        List<BabyTrackingEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM baby_tracking ORDER BY time DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BabyTrackingEntry entry = new BabyTrackingEntry(
                        rs.getString("type"),
                        rs.getString("value"),
                        rs.getTimestamp("time").toLocalDateTime().toString()
                );
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving entries: " + e.getMessage());
        }
        return entries;
    }

    // ✅ Metode til at gemme en baby-tracking entry i databasen
    public void saveEntry(BabyTrackingEntry entry) {
        String sql = "INSERT INTO baby_tracking (type, value, time) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entry.getType());
            stmt.setString(2, entry.getValue());
            stmt.setTimestamp(3, Timestamp.valueOf(entry.getTime()));

            stmt.executeUpdate();
            System.out.println("✅ Entry saved: " + entry);
        } catch (SQLException e) {
            System.err.println("❌ Error saving entry: " + e.getMessage());
        }
    }
}
