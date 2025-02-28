package app.controllers;

import app.services.BabyTrackingService;
import app.utils.RequestParser;
import app.controllers.DatabaseController;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.JsonNode;

public class BabyTrackingController {
    private final BabyTrackingService babyTrackingService;
    private final RequestParser requestParser;
    private final DatabaseController dbController;

    public BabyTrackingController(BabyTrackingService babyTrackingService, RequestParser requestParser, DatabaseController dbController) {
        this.babyTrackingService = babyTrackingService;
        this.requestParser = requestParser;
        this.dbController = dbController;
    }

    public void registerRoutes(Javalin app) {
        app.get("/babytracking", this::getAllEntries);
        app.post("/babytracking", this::addEntry);
    }

    private void getAllEntries(Context ctx) {
        ctx.json(babyTrackingService.getAllEntries());
    }

    private void addEntry(Context ctx) {
        try {
            String body = ctx.body();
            JsonNode jsonNode = RequestParser.parseJson(body);

            if (!RequestParser.validateBabyTrackingEntry(jsonNode)) {
                ctx.status(400).result("❌ Invalid JSON format: Missing required fields.");
                return;
            }

            babyTrackingService.addEntry(body);
            ctx.status(201).result("✅ Entry successfully added!");
        } catch (Exception e) {
            ctx.status(400).result("❌ Error processing request: " + e.getMessage());
        }
    }
}
