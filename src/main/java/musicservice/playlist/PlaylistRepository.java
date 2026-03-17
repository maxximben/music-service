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

    public void setTitle(String title, int playlistId) {
        String query = "update playlists set title = ? where playlist_id = ?";
        jdbcTemplate.update(query, title, playlistId);
    }

    public void addSong(int songId, int playlistId) {
        String query = "INSERT INTO playlist_songs (playlist_id, song_id, position) SELECT ?, ?, COALESCE(MAX(position), 0) + 1 FROM playlist_songs WHERE playlist_id = ?";
        jdbcTemplate.update(query, playlistId, songId, playlistId);
    }


    public void deleteSong(int songId, int playlistId) {
        String query = "delete from playlist_songs where playlist_id = ? and song_id = ?";
        jdbcTemplate.update(query, playlistId, songId);
    }
}
