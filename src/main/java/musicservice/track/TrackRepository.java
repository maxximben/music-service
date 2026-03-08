package musicservice.track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TrackRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void addTrack(Track track) {
        String query = "insert into songs (title, user_id, album, cover, url) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, track.title(), track.userId(), track.album(), track.cover(), track.url());
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
