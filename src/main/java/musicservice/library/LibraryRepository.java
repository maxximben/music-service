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
                WHERE p.is_album = true
                  AND (
                      p.user_id = ?
                      OR EXISTS (
                          SELECT 1
                          FROM users_playlists up
                          WHERE up.playlist_id = p.playlist_id
                            AND up.user_id = ?
                      )
                  )

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
                id, id, id, id
        );

        return new Library(items);
    }

    public boolean albumExists(int albumId) {
        String query = "select exists(select 1 from playlists where playlist_id = ? and is_album = true)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, albumId));
    }

    public void addAlbumToLibrary(int userId, int albumId) {
        String query = """
                INSERT INTO users_playlists (user_id, playlist_id, is_owner, added_at)
                VALUES (?, ?, false, NOW())
                ON CONFLICT (user_id, playlist_id) DO NOTHING
                """;
        jdbcTemplate.update(query, userId, albumId);
    }
}
