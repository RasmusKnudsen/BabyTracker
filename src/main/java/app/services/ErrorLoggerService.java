package app.services;

import java.time.LocalDateTime;

public class ErrorLoggerService {
    public ErrorLoggerService() {
        // Konstruktor uden argumenter
    }

    public void logError(String message, Exception e) {
        System.err.println("❌ [" + LocalDateTime.now() + "] ERROR: " + message);
        e.printStackTrace();
    }
}
