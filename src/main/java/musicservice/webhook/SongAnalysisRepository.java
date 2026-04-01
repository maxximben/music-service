package musicservice.webhook;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SongAnalysisRepository {

    private final JdbcTemplate jdbcTemplate;

    public SongAnalysisRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Integer> findTop20SimilarSongIdsByDiscogs(int songId) {
        String sql = """
                WITH source_song AS (
                    SELECT discogs->'tags' AS source_tags
                    FROM song_analysis
                    WHERE song_id = ?
                ),
                similarities AS (
                    SELECT
                        sa.song_id,
                        COALESCE((
                            SELECT
                                SUM((src.value::double precision) * (cand.value::double precision))
                                /
                                (
                                    SQRT(SUM(POWER(src.value::double precision, 2)))
                                    *
                                    SQRT(SUM(POWER(cand.value::double precision, 2)))
                                )
                            FROM jsonb_each_text((SELECT source_tags FROM source_song)) src
                            JOIN jsonb_each_text(sa.discogs->'tags') cand
                              ON src.key = cand.key
                        ), 0.0) AS similarity
                    FROM song_analysis sa
                    WHERE sa.song_id <> ?
                )
                SELECT song_id
                FROM similarities
                WHERE similarity > 0
                ORDER BY similarity DESC, song_id
                LIMIT 20
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getInt("song_id"),
                songId,
                songId
        );
    }

    public List<String> findTop20SimilarSongTitlesByDiscogs(int songId) {
        String sql = """
                WITH source_song AS (
                    SELECT discogs->'tags' AS source_tags
                    FROM song_analysis
                    WHERE song_id = ?
                ),
                similarities AS (
                    SELECT
                        sa.song_id,
                        COALESCE((
                            SELECT
                                SUM((src.value::double precision) * (cand.value::double precision))
                                /
                                (
                                    SQRT(SUM(POWER(src.value::double precision, 2)))
                                    *
                                    SQRT(SUM(POWER(cand.value::double precision, 2)))
                                )
                            FROM jsonb_each_text((SELECT source_tags FROM source_song)) src
                            JOIN jsonb_each_text(sa.discogs->'tags') cand
                              ON src.key = cand.key
                        ), 0.0) AS similarity
                    FROM song_analysis sa
                    WHERE sa.song_id <> ?
                )
                SELECT s.title
                FROM similarities sim
                JOIN songs s ON s.song_id = sim.song_id
                WHERE sim.similarity > 0
                ORDER BY sim.similarity DESC, sim.song_id
                LIMIT 7
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("title"),
                songId,
                songId
        );
    }
}