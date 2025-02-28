package app.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Metode til at validere og parse JSON-input fra en HTTP-request
    public static JsonNode parseJson(String json) throws Exception {
        return objectMapper.readTree(json);
    }

    // Metode til at validere, at et JSON-object har nødvendige felter
    public static boolean validateBabyTrackingEntry(JsonNode jsonNode) {
        return jsonNode.has("type") && jsonNode.has("value") && jsonNode.has("time");
    }
}
