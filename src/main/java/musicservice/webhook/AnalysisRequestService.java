package musicservice.webhook;

import musicservice.song.Song;
import musicservice.song.SongRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class AnalysisRequestService {

    private final SongRepository songRepository;
    private final AnalysisRepository analysisRepository;
    private final RestClient restClient;

    @Value("${analysis.fastapi-base-url}")
    private String fastApiBaseUrl;

    @Value("${analysis.callback-url}")
    private String callbackUrl;

    public AnalysisRequestService(
            SongRepository songRepository,
            AnalysisRepository analysisRepository
    ) {
        this.songRepository = songRepository;
        this.analysisRepository = analysisRepository;
        this.restClient = RestClient.create();
    }

    public String startAnalysis(int songId) {
        Song song = songRepository.getSongById(songId);
        if (song == null) {
            throw new IllegalArgumentException("Песня с id=" + songId + " не найдена");
        }

        UUID jobId = UUID.randomUUID();

        analysisRepository.createJob(jobId, songId, "pending");

        StartAnalysisRequest request = new StartAnalysisRequest(
                jobId.toString(),
                song.url(),
                callbackUrl
        );

        StartAnalysisResponse response = restClient.post()
                .uri(fastApiBaseUrl + "/analyze")
                .body(request)
                .retrieve()
                .body(StartAnalysisResponse.class);

        if (response == null) {
            analysisRepository.updateJobStatus(jobId.toString(), "failed", "FastAPI вернул пустой ответ");
            throw new RuntimeException("FastAPI вернул пустой ответ");
        }

        return jobId.toString();
    }
}