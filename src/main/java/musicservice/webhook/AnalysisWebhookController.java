package musicservice.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class AnalysisWebhookController {

    private final AnalysisRepository analysisRepository;

    public AnalysisWebhookController(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    @PostMapping("/music-result")
    public ResponseEntity<String> receiveAnalysisResult(@RequestBody AnalysisResult result) {


        System.out.println(result.toString());


        System.out.println(result.getJobId());
        Integer songId = analysisRepository.findSongIdByJobId(result.getJobId());
//        songId = 2;

        System.out.println("получен результат");
        System.out.println(songId);

        if (songId == null) {
            return ResponseEntity.status(202).body("Job not found yet: " + result.getJobId());
        }

        System.out.println("перед update job status");

        analysisRepository.updateJobStatus(
                result.getJobId(),
                result.getStatus(),
                result.getError()
        );

        System.out.println("перед if");

        if ("completed".equals(result.getStatus())) {
            analysisRepository.saveAnalysisResult(songId, result);
        }

        return ResponseEntity.ok("Webhook processed");
    }
}