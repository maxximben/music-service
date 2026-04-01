package musicservice.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AnalysisRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public AnalysisRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void createJob(UUID jobId, int songId, String status) {
        String sql = """
                INSERT INTO analysis_jobs (job_id, song_id, status, error, created_at, updated_at)
                VALUES (?, ?, ?, NULL, NOW(), NOW())
                """;
        jdbcTemplate.update(sql, jobId, songId, status);
    }

    public Integer findSongIdByJobId(String jobId) {
        String sql = "SELECT song_id FROM analysis_jobs WHERE job_id = ?::uuid";
        List<Integer> result = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getInt("song_id"),
                jobId
        );

        return result.isEmpty() ? null : result.getFirst();
    }

    public boolean existsJob(String jobId) {
        String sql = "SELECT COUNT(*) FROM analysis_jobs WHERE job_id = ?::uuid";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, jobId);
        return count != null && count > 0;
    }

    public void updateJobStatus(String jobId, String status, String error) {
        String sql = """
                UPDATE analysis_jobs
                SET status = ?, error = ?, updated_at = NOW()
                WHERE job_id = ?::uuid
                """;
        jdbcTemplate.update(sql, status, error, jobId);
    }

    public void saveAnalysisResult(int songId, AnalysisResult result) {

        System.out.println("saveAnalysisResult(int songId, AnalysisResult result)");

        try {
            String musicnnJson = objectMapper.writeValueAsString(result.getMusicnn());
            String discogsJson = objectMapper.writeValueAsString(result.getDiscogs());

            String sql = """
                    INSERT INTO song_analysis(song_id, audio_length_seconds, musicnn, discogs, analyzed_at)
                    VALUES (?, ?, ?::jsonb, ?::jsonb, NOW())
                    ON CONFLICT (song_id)
                    DO UPDATE SET
                        audio_length_seconds = EXCLUDED.audio_length_seconds,
                        musicnn = EXCLUDED.musicnn,
                        discogs = EXCLUDED.discogs,
                        analyzed_at = NOW()
                    """;

            jdbcTemplate.update(
                    sql,
                    songId,
                    result.getAudioLengthSeconds(),
                    musicnnJson,
                    discogsJson
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось сериализовать результат анализа", e);
        }
    }
}