package musicservice.library;

import musicservice.search.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LibraryRepository {

    private final JdbcTemplate jdbcTemplate;

    public LibraryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Library findItemsByUserId(int id) {
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
                       'Track' AS type,
                       COALESCE(ta.author, '') AS author,
                       s.song_id AS id
                FROM songs s
                LEFT JOIN track_authors ta ON ta.song_id = s.song_id
                WHERE EXISTS (
                    SELECT 1
                    FROM song_authors sa
                    WHERE sa.song_id = s.song_id
                      AND sa.user_id = ?
                )

                UNION ALL

                SELECT p.cover AS cover,
                       p.title AS name,
                       'Album' AS type,
                       u.username AS author,
                       p.playlist_id AS id
                FROM playlists p
                JOIN users u ON p.user_id = u.user_id
                WHERE p.user_id = ?
                  AND p.is_album = true

                UNION ALL

                SELECT p.cover AS cover,
                       p.title AS name,
                       'Playlist' AS type,
                       u.username AS author,
                       p.playlist_id AS id
                FROM playlists p
                JOIN users u ON p.user_id = u.user_id
                WHERE p.user_id = ?
                  AND (p.is_album = false OR p.is_album IS NULL)
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
                id, id, id
        );

        return new Library(items);
    }
}
