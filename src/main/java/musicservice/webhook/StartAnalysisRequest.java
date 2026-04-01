package musicservice.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StartAnalysisRequest(
        @JsonProperty("job_id") String jobId,
        @JsonProperty("audio_url") String audioUrl,
        @JsonProperty("callback_url") String callbackUrl
) {}