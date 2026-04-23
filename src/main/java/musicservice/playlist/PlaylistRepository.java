package musicservice.playlist;

import musicservice.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class PlaylistRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SongRepository songRepository;

    public void createPlaylist(String title, int userId) {
        String query = "insert into playlists (title, user_id, is_album, is_private, count_of_songs) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, title, userId, false, true, 0);
    }

    public int createPlaylistWithSongs(String title, int userId, List<Integer> songIds) {
        String insertPlaylistQuery = """
            INSERT INTO playlists (title, user_id, is_album, is_private, count_of_songs)
            VALUES (?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertPlaylistQuery,
                    new String[]{"playlist_id"}
            );
            ps.setString(1, title);
            ps.setInt(2, userId);
            ps.setBoolean(3, false);
            ps.setBoolean(4, true);
            ps.setInt(5, songIds.size());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys == null || keys.get("playlist_id") == null) {
            throw new IllegalStateException("Не удалось получить id созданного плейлиста");
        }

        int playlistId = ((Number) keys.get("playlist_id")).intValue();

        String insertSongQuery = """
            INSERT INTO playlist_songs (playlist_id, song_id, position)
            VALUES (?, ?, ?)
            """;

        for (int i = 0; i < songIds.size(); i++) {
            jdbcTemplate.update(insertSongQuery, playlistId, songIds.get(i), i + 1);
        }

        return playlistId;
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

    public Playlist getPlaylistById(int id) {
        String query = "select * from playlists where playlist_id = ?";
        return jdbcTemplate.queryForObject(query, playlistRowMapper(), id);
    }

    public RowMapper<Playlist> playlistRowMapper() {
        return (rs, rowNum) -> new Playlist(
                rs.getInt("playlist_id"),
                rs.getString("title"),
                rs.getString("cover"),
                rs.getInt("count_of_songs"),
                songRepository.findSongsByPlaylistId(rs.getInt("playlist_id"))
        );
    }


}
