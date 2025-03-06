package app.services;

import app.models.BabyTrackingEntry;
import app.controllers.DatabaseController;
import java.util.List;

public class BabyTrackingService {
    private final DatabaseController dbController;

    public BabyTrackingService(DatabaseController dbController) {
        this.dbController = dbController;
    }

    // ✅ Opdateret metode: Forventer nu et BabyTrackingEntry-objekt
    public void addEntry(BabyTrackingEntry entry) {
        dbController.saveEntry(entry); // Gemmer objektet direkte i databasen
    }

    public List<BabyTrackingEntry> getAllEntries() {
        return dbController.getAllBabyEntries();
    }
}
