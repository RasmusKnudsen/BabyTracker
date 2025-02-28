package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.BabyTrackingController;
import app.controllers.DatabaseController;
import app.services.BabyTrackingService;
import app.services.ErrorLoggerService;
import app.utils.RequestParser;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public"); // For CSS, JS, billeder
            config.jetty.sessionHandler(SessionConfig.sessionConfig());
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(8080);

        // Database og services
        DatabaseController dbController = new DatabaseController();
        dbController.initialize();

        RequestParser requestParser = new RequestParser();
        ErrorLoggerService errorLogger = new ErrorLoggerService(); // Nu uden argument
        BabyTrackingService babyTrackingService = new BabyTrackingService(dbController);

        // Controllers
        BabyTrackingController babyTrackingController = new BabyTrackingController(babyTrackingService, requestParser, dbController);

        // Routing
        app.get("/", ctx -> ctx.render("index.html"));
        app.get("/babytracking-ui", ctx -> ctx.render("babytracking.html")); // ✅ Ny route for BabyTracking UI

        // Registrer routes fra controlleren
        babyTrackingController.registerRoutes(app);
    }
}
