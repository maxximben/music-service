package musicservice.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")

public class AnalysisWebhookController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/music-result")
    public ResponseEntity<Void> receiveAnalysisResult(@RequestBody AnalysisResult result) {
        IO.println("Получен результат");
        analysisService.saveResult(result);
        return ResponseEntity.ok().build();   // 200 OK — важно, чтобы FastAPI не считал это ошибкой
    }

    // Для отладки / просмотра результата (можно убрать в продакшене или защитить)
    @GetMapping("/result/{jobId}")
    public ResponseEntity<AnalysisResult> getResult(@PathVariable String jobId) {
        AnalysisResult result = analysisService.getResult(jobId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}