package app.services;

import app.controllers.DatabaseController;
import app.models.BabyTrackingEntry;
import java.util.List;

public class BabyTrackingService {
    private final DatabaseController dbController;

    public BabyTrackingService(DatabaseController dbController) {
        this.dbController = dbController;
    }

    // ✅ Hent alle baby-tracking entries
    public List<BabyTrackingEntry> getAllEntries() {
        return dbController.getAllBabyEntries();
    }

    // ✅ Tilføj en ny entry til databasen
    public void addEntry(String jsonEntry) {
        try {
            BabyTrackingEntry entry = BabyTrackingEntry.fromJson(jsonEntry);
            dbController.saveEntry(entry);
        } catch (Exception e) {
            System.err.println("❌ Fejl ved tilføjelse af entry: " + e.getMessage());
        }
    }
}
