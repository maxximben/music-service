package musicservice.track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.Map;

@Repository
public class TrackRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void addTrack(Track track) {
        if (track.authorIds().isEmpty()) {
            throw new IllegalArgumentException("У трека должен быть хотя бы один автор");
        }

        String query = "insert into songs (title, album, cover, url) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"song_id"});
            ps.setString(1, track.title());
            ps.setString(2, track.album());
            ps.setString(3, track.cover());
            ps.setString(4, track.url());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys == null || keys.get("song_id") == null) {
            throw new IllegalStateException("Не удалось получить id созданного трека");
        }

        int songId = ((Number) keys.get("song_id")).intValue();
        String authorQuery = "insert into song_authors (song_id, user_id, author_order) values (?, ?, ?)";
        for (int i = 0; i < track.authorIds().size(); i++) {
            jdbcTemplate.update(authorQuery, songId, track.authorIds().get(i), i + 1);
        }
    }


    public String getTrackById(int id) {
        String query = "select url from songs where song_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }

    public void deleteById(int id) {
        String query = "delete from songs where song_id = ?";
        jdbcTemplate.update(query, id);
    }
}
