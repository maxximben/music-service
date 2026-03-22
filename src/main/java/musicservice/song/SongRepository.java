package musicservice.song;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class SongRepository {

    private final JdbcTemplate jdbcTemplate;

    public SongRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Song getSongById(int id) {
        String query = "select * from songs where song_id = ?";
        return jdbcTemplate.queryForObject(query, songRowMapper(), id);
    }

    public String getAuthorBySongId(int id) {
        String query = "select u.username from songs s join users u on s.user_id = u.user_id where s.song_id = ?" ;
        return String.valueOf(new ArrayList<>(jdbcTemplate.query(query, author(), id)).getFirst());
    }

    private RowMapper<Song> songRowMapper() {
        return (rs, rowNum) -> new Song (
                rs.getInt("song_id"),
                rs.getString("title"),
                rs.getInt("user_id"),
                rs.getString("cover"),
                rs.getString("url"),
                rs.getInt("duration"),
                getAuthorBySongId(rs.getInt("song_id"))
        );
    }

    private RowMapper<String> author() {
        return (rs, rowNum) -> rs.getString("username");
    }
}
