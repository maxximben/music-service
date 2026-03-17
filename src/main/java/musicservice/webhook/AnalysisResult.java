package musicservice.webhook;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnalysisResult {
    private String jobId;
    private String status;
    private Double audioLengthSeconds;
    private MusicNN musicnn;
    private Discogs discogs;
    private String error;
}

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class MusicNN {
    private String model;
    private Integer totalTags;
    private Map<String, Double> tags;
}

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class Discogs {
    private String model;
    private Integer totalTags;
    private Map<String, Double> tags;
}