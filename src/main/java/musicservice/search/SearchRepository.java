package musicservice.search;

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
                WITH track_authors AS (
                    SELECT sa.song_id,
                           string_agg(u.username, ', ' ORDER BY sa.author_order, u.username) AS author
                    FROM song_authors sa
                    JOIN users u ON u.user_id = sa.user_id
                    GROUP BY sa.song_id
                )
                SELECT s.cover AS cover,
                       s.title AS name,
                       s.song_id AS id,
                       'Track' AS type,
                       COALESCE(ta.author, '') AS author
                FROM songs s
                LEFT JOIN track_authors ta ON ta.song_id = s.song_id
                WHERE LOWER(s.title) LIKE LOWER(?)
                   OR LOWER(COALESCE(ta.author, '')) LIKE LOWER(?)

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

        List<Item> items = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Item(
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
