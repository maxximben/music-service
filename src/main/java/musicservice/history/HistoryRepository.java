package musicservice.history;

import musicservice.search.Item;
import musicservice.search.SearchResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public HistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePlayedSong(int userId, int songId) {
        String insertSql = """
                INSERT INTO listening_history(user_id, song_id)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(insertSql, userId, songId);

        String deleteOldSql = """
                DELETE FROM listening_history
                WHERE history_id IN (
                    SELECT history_id
                    FROM listening_history
                    WHERE user_id = ?
                    ORDER BY played_at DESC, history_id DESC
                    OFFSET 30
                )
                """;

        jdbcTemplate.update(deleteOldSql, userId);
    }

    public SearchResult getSearchHistoryByUserId(int userId) {
        String sql = """
                SELECT s.cover AS cover,
                       s.title AS name,
                       'Track' AS type,
                       u.username AS author,
                       s.song_id AS id
                FROM listening_history h
                JOIN songs s ON h.song_id = s.song_id
                JOIN users u ON s.user_id = u.user_id
                WHERE h.user_id = ?
                ORDER BY h.played_at DESC, h.history_id DESC
                LIMIT 30
                """;

        List<Item> items = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Item(
                        rs.getString("cover"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("author"),
                        rs.getInt("id")
                ),
                userId
        );

        return new SearchResult(items);
    }
}