package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.BabyTrackingController;
import app.controllers.DatabaseController;
import app.services.BabyTrackingService;
import app.services.ErrorLoggerService;
import app.utils.RequestParser;
import app.models.BabyTrackingEntry;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Main {
    public static void main(String[] args) {
        // ✅ Test JSON-parsing FØR serveren startes
        testJsonParsing();

        // ✅ Initialiser Jackson ObjectMapper til at understøtte Java 8 datoer (LocalDateTime)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Formatér datoer som ISO 8601 strings

        // ✅ Initialiserer Javalin og Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public"); // For CSS, JS, billeder
            config.jetty.sessionHandler(SessionConfig.sessionConfig());
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(8080);

        // ✅ Database og services
        DatabaseController dbController = new DatabaseController();
        dbController.initialize();

        RequestParser requestParser = new RequestParser();
        ErrorLoggerService errorLogger = new ErrorLoggerService();
        BabyTrackingService babyTrackingService = new BabyTrackingService(dbController);

        // ✅ Controllers
        BabyTrackingController babyTrackingController = new BabyTrackingController(babyTrackingService, requestParser);

        // ✅ Routing
        app.get("/", ctx -> ctx.render("index.html"));
        app.get("/babytracking-ui", ctx -> ctx.render("babytracking.html")); // ✅ Ny route for BabyTracking UI

        // ✅ Registrer routes fra controlleren
        babyTrackingController.registerRoutes(app);
    }

    // ✅ Test JSON Parsing før serveren startes
    private static void testJsonParsing() {
        try {
            String testJson = """
        {
            "type": "Amning",
            "startTime": "2025-03-06T12:18:00",
            "endTime": "2025-03-06T13:19:00",
            "totalTime": 61,
            "leftBreastTime": 30,
            "rightBreastTime": 31,
            "lastBreastUsed": "Venstre"
        }
        """;

            BabyTrackingEntry entry = BabyTrackingEntry.fromJson(testJson);
            System.out.println("✅ JSON-test lykkedes: " + entry);

            // 👇 Ekstra test: Udskriv den serialiserede JSON igen
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String serializedJson = objectMapper.writeValueAsString(entry);
            System.out.println("✅ JSON efter parsing: " + serializedJson);

        } catch (Exception e) {
            System.err.println("❌ Fejl ved parsing af test-JSON: " + e.getMessage());
            throw new RuntimeException("Fejl i JSON-test! Fix dette, før du starter serveren."); // Stopper programmet
        }
    }
}
