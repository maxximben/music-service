package musicservice.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlaylistRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createPlaylist(String title, int userId) {

        String query = "insert into playlists (title, user_id, is_album, is_private, count_of_songs) values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(query, title, userId, false, true, 0);
    }

    public void setPlaylistCover(String url, int playlistId) {
        String query = "update playlists set cover = ? where playlist_id = ?";
        jdbcTemplate.update(query, url, playlistId);
    }




}
