package app.config;

import org.eclipse.jetty.server.session.SessionHandler;
import java.util.function.Supplier;

public class SessionConfig {
    public static Supplier<SessionHandler> sessionConfig() {
        return SessionHandler::new; // Returnerer en Supplier, som Javalin 5 kræver
    }
}
