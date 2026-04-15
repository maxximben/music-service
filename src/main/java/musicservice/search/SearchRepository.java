package musicservice.search;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepository {

    private final JdbcTemplate jdbcTemplate;

    public SearchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SearchResult findItemsByName(String name) {
        String pattern = "%" + name + "%";

        String sql = """
                SELECT s.cover AS cover,
                       s.title AS name,
                       s.song_id AS id,
                       'Track' AS type,
                       u.username AS author
                FROM songs s
                JOIN users u ON s.user_id = u.user_id
                WHERE LOWER(s.title) LIKE LOWER(?)
                   OR LOWER(u.username) LIKE LOWER(?)

                UNION ALL

                SELECT p.cover AS cover,
                       p.title AS name,
                       p.playlist_id AS id,
                       'Album' AS type,
                       u.username AS author
                FROM playlists p
                JOIN users u ON p.user_id = u.user_id
                WHERE p.is_album = true
                  AND (
                      LOWER(p.title) LIKE LOWER(?)
                      OR LOWER(u.username) LIKE LOWER(?)
                  )

                UNION ALL

                SELECT p.cover AS cover,
                       p.title AS name,
                       p.playlist_id AS id,
                       'Playlist' AS type,
                       u.username AS author
                FROM playlists p
                JOIN users u ON p.user_id = u.user_id
                WHERE (p.is_album = false OR p.is_album IS NULL)
                  AND (
                      LOWER(p.title) LIKE LOWER(?)
                      OR LOWER(u.username) LIKE LOWER(?)
                  )
                """;

        List<SearchItem> items = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new SearchItem(
                        rs.getString("cover"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("author"),
                        rs.getInt("id")
                ),
                pattern, pattern,
                pattern, pattern,
                pattern, pattern
        );

        return new SearchResult(items);
    }

}
