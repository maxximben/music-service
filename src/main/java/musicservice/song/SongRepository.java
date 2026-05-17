package musicservice.song;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class SongRepository {

    private final JdbcTemplate jdbcTemplate;

    public SongRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Song getSongById(int id) {
        String query = """
                SELECT s.song_id,
                       s.title,
                       s.cover,
                       s.url,
                       s.duration,
                       COALESCE(string_agg(u.username, ', ' ORDER BY sa.author_order, u.username), '') AS author,
                       COALESCE(
                           array_agg(u.username ORDER BY sa.author_order, u.username)
                               FILTER (WHERE u.username IS NOT NULL),
                           ARRAY[]::text[]
                       ) AS authors
                FROM songs s
                LEFT JOIN song_authors sa ON sa.song_id = s.song_id
                LEFT JOIN users u ON u.user_id = sa.user_id
                WHERE s.song_id = ?
                GROUP BY s.song_id, s.title, s.cover, s.url, s.duration
                """;
        return jdbcTemplate.queryForObject(query, songRowMapper(), id);
    }

//    public List<Song> getSongsByPlaylistId() {
//        String query = "select "
//        return jdbcTemplate.queryForList(qu)
//    }

    public String getAuthorBySongId(int id) {
        String query = """
                SELECT COALESCE(string_agg(u.username, ', ' ORDER BY sa.author_order, u.username), '')
                FROM song_authors sa
                JOIN users u ON u.user_id = sa.user_id
                WHERE sa.song_id = ?
                """;
        return jdbcTemplate.queryForObject(query, String.class, id);
    }


    public List<Song> findSongsByPlaylistId(int playlistId) {
        String sql = """
                SELECT s.song_id,
                       s.title,
                       s.cover,
                       s.url,
                       s.duration,
                       COALESCE(string_agg(u.username, ', ' ORDER BY sa.author_order, u.username), '') AS author,
                       COALESCE(
                           array_agg(u.username ORDER BY sa.author_order, u.username)
                               FILTER (WHERE u.username IS NOT NULL),
                           ARRAY[]::text[]
                       ) AS authors
                FROM songs s
                INNER JOIN playlist_songs ps ON s.song_id = ps.song_id
                LEFT JOIN song_authors sa ON sa.song_id = s.song_id
                LEFT JOIN users u ON u.user_id = sa.user_id
                WHERE ps.playlist_id = ?
                GROUP BY s.song_id, s.title, s.cover, s.url, s.duration, ps.position
                ORDER BY ps.position
                """;
        return jdbcTemplate.query(sql, songRowMapper(), playlistId);
    }


    private RowMapper<Song> songRowMapper() {
        return (rs, rowNum) -> new Song (
                rs.getInt("song_id"),
                rs.getString("title"),
                rs.getString("cover"),
                rs.getString("url"),
                (Integer) rs.getObject("duration"),
                rs.getString("author"),
                toStringList(rs.getArray("authors"))
        );
    }

    private List<String> toStringList(Array sqlArray) throws SQLException {
        if (sqlArray == null) {
            return Collections.emptyList();
        }

        String[] values = (String[]) sqlArray.getArray();
        return List.of(values);
    }
}
