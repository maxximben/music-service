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
                SELECT s.cover AS cover,
                       s.title AS name,
                       'Track' AS type,
                       u.username AS author,
                       s.song_id AS id
                FROM songs s
                JOIN users u ON s.user_id = u.user_id
                WHERE s.user_id = ?

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
