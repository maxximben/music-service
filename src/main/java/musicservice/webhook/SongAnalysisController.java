package musicservice.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongAnalysisController {

    private final AnalysisRequestService analysisRequestService;

    public SongAnalysisController(AnalysisRequestService analysisRequestService) {
        this.analysisRequestService = analysisRequestService;
    }

    @PostMapping("/{songId}/analyze")
    public ResponseEntity<Map<String, String>> analyzeSong(@PathVariable int songId) {
        String jobId = analysisRequestService.startAnalysis(songId);

        return ResponseEntity.ok(Map.of(
                "message", "Анализ запущен",
                "jobId", jobId
        ));
    }
}