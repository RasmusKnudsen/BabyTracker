package app.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BabyTrackingEntry {
    private String type;  // "sleep", "feed", "diaper"
    private String value; // "Højre bryst", "2 timer", "Tis + afføring"
    private String time;  // ISO 8601 format: "2024-02-27T14:30:00"

    public BabyTrackingEntry() {}

    public BabyTrackingEntry(String type, String value, String time) {
        this.type = type;
        this.value = value;
        this.time = time;
    }

    public String getType() { return type; }
    public String getValue() { return value; }
    public String getTime() { return time; }

    public static BabyTrackingEntry fromJson(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, BabyTrackingEntry.class);
    }

    @Override
    public String toString() {
        return "BabyTrackingEntry{type='" + type + "', value='" + value + "', time='" + time + "'}";
    }
}
