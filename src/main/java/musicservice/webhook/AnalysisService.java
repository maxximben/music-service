package musicservice.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    // In-memory хранилище (в продакшене → Redis / PostgreSQL / Mongo)
    private final Map<String, AnalysisResult> results = new ConcurrentHashMap<>();

    public void saveResult(AnalysisResult result) {
        if ("completed".equals(result.getStatus())) {
            log.info("Успешно получен анализ для jobId = {}, длина аудио = {} сек",
                    result.getJobId(), result.getAudioLengthSeconds());
        } else if ("failed".equals(result.getStatus())) {
            log.error("Анализ провалился для jobId = {} → {}", result.getJobId(), result.getError());
        }

        results.put(result.getJobId(), result);
    }

    public AnalysisResult getResult(String jobId) {
        return results.get(jobId);
    }
}