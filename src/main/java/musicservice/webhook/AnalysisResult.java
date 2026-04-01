package musicservice.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnalysisResult {
    @JsonProperty("job_id")
    private String jobId;
    private String status;
    @JsonProperty("audio_length_seconds")
    private Double audioLengthSeconds;
    private MusicNN musicnn;
    private Discogs discogs;
    private String error;
}

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class MusicNN {
    private String model;
    @JsonProperty("total_tags")
    private Integer totalTags;
    private Map<String, Double> tags;
}

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class Discogs {
    private String model;
    @JsonProperty("total_tags")
    private Integer totalTags;
    private Map<String, Double> tags;
}