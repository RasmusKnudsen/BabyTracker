package app.controllers;

import app.models.BabyTrackingEntry;
import app.services.BabyTrackingService;
import app.utils.RequestParser;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class BabyTrackingController {
    private final BabyTrackingService babyTrackingService;
    private final RequestParser requestParser;

    public BabyTrackingController(BabyTrackingService babyTrackingService, RequestParser requestParser) {
        this.babyTrackingService = babyTrackingService;
        this.requestParser = requestParser;
    }

    public void registerRoutes(Javalin app) {
        app.get("/babytracking", this::getAllEntries);
        app.post("/babytracking", this::addEntry);
        app.post("/amning", this::addBreastfeedingEntry); // ✅ Route til amning
        app.get("/debug/amning", this::debugAmningEntries); // ✅ Debug route til at se gemte entries
    }

    private void getAllEntries(Context ctx) {
        ctx.json(babyTrackingService.getAllEntries());
    }

    private void addEntry(Context ctx) {
        try {
            String body = ctx.body();
            System.out.println("DEBUG: Modtaget JSON fra frontend -> " + body); // 🔍 Debug input JSON

            BabyTrackingEntry entry = BabyTrackingEntry.fromJson(body);
            babyTrackingService.addEntry(entry);

            ctx.status(201).result("✅ Entry successfully added!");
        } catch (Exception e) {
            System.err.println("❌ Fejl ved parsing af JSON: " + e.getMessage()); // 🔍 Debug fejl
            ctx.status(400).result("❌ Fejl ved gemning af entry: " + e.getMessage());
        }
    }

    private void addBreastfeedingEntry(Context ctx) {
        try {
            String body = ctx.body();
            System.out.println("DEBUG: Modtaget JSON fra frontend -> " + body); // 🔍 Debug input JSON

            BabyTrackingEntry entry = BabyTrackingEntry.fromJson(body);
            babyTrackingService.addEntry(entry);

            ctx.status(201).result("✅ Amning entry gemt!");
        } catch (Exception e) {
            System.err.println("❌ Fejl ved parsing af JSON: " + e.getMessage()); // 🔍 Debug fejl
            ctx.status(400).result("❌ Fejl ved gemning af amning: " + e.getMessage());
        }
    }

    private void debugAmningEntries(Context ctx) {
        ctx.json(babyTrackingService.getAllEntries()); // ✅ Returnerer alle entries som JSON
    }
}
