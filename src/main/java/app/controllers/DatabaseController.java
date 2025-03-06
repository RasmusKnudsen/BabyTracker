package app.controllers;

import app.models.BabyTrackingEntry;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.LocalDateTime;

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
        String sql = "SELECT * FROM baby_tracking ORDER BY start_time DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Timestamp startTimestamp = rs.getTimestamp("start_time");
                Timestamp endTimestamp = rs.getTimestamp("end_time");

                // ✅ Konverter kun hvis værdien ikke er null
                LocalDateTime startTime = (startTimestamp != null) ? startTimestamp.toLocalDateTime() : null;
                LocalDateTime endTime = (endTimestamp != null) ? endTimestamp.toLocalDateTime() : null;

                BabyTrackingEntry entry = new BabyTrackingEntry(
                        rs.getString("type"),
                        startTime,
                        endTime,
                        rs.getInt("total_time"),
                        rs.getInt("left_breast_time"),
                        rs.getInt("right_breast_time"),
                        rs.getString("last_breast_used")
                );
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving entries: " + e.getMessage());
        }
        return entries;
    }





    // ✅ Gem en amnings-entry i databasen
    public void saveEntry(BabyTrackingEntry entry) {
        String sql = "INSERT INTO baby_tracking (type, value, start_time, end_time, total_time, left_breast_time, right_breast_time, last_breast_used) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entry.getType());
            stmt.setString(2, "amning-data"); // Dummy værdi for at undgå NULL-fejl
            stmt.setTimestamp(3, Timestamp.valueOf(entry.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(entry.getEndTime()));
            stmt.setInt(5, entry.getTotalTime());
            stmt.setInt(6, entry.getLeftBreastTime());
            stmt.setInt(7, entry.getRightBreastTime());
            stmt.setString(8, entry.getLastBreastUsed());

            stmt.executeUpdate();
            System.out.println("✅ Amning entry saved: " + entry);
        } catch (SQLException e) {
            System.err.println("❌ Error saving entry: " + e.getMessage());
        }
    }

}
