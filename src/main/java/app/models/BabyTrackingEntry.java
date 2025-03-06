package app.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class BabyTrackingEntry {
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime endTime;

    private int totalTime;
    private int leftBreastTime;
    private int rightBreastTime;
    private String lastBreastUsed;

    // ✅ Standard constructor (Jackson kræver dette)
    public BabyTrackingEntry() {
    }

    // ✅ Constructor med Jackson-annotationer
    @JsonCreator
    public BabyTrackingEntry(
            @JsonProperty("type") String type,
            @JsonProperty("startTime") LocalDateTime startTime,
            @JsonProperty("endTime") LocalDateTime endTime,
            @JsonProperty("totalTime") int totalTime,
            @JsonProperty("leftBreastTime") int leftBreastTime,
            @JsonProperty("rightBreastTime") int rightBreastTime,
            @JsonProperty("lastBreastUsed") String lastBreastUsed) {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.leftBreastTime = leftBreastTime;
        this.rightBreastTime = rightBreastTime;
        this.lastBreastUsed = lastBreastUsed;
    }

    // ✅ Metode til at parse JSON til et objekt
    public static BabyTrackingEntry fromJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule()); // 💡 Tilføj support for Java 8 time
        return objectMapper.readValue(json, BabyTrackingEntry.class);
    }

    // ✅ Getters (Jackson bruger disse til at serialisere objektet)
    public String getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getLeftBreastTime() {
        return leftBreastTime;
    }

    public int getRightBreastTime() {
        return rightBreastTime;
    }

    public String getLastBreastUsed() {
        return lastBreastUsed;
    }

    // ✅ Tilføj en `toString()` metode til debugging
    @Override
    public String toString() {
        return "BabyTrackingEntry{" +
                "type='" + type + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalTime=" + totalTime +
                ", leftBreastTime=" + leftBreastTime +
                ", rightBreastTime=" + rightBreastTime +
                ", lastBreastUsed='" + lastBreastUsed + '\'' +
                '}';
    }
}
