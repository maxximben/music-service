package musicservice.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    @Autowired
    PlaylistRepository playlistRepository;

    public Playlist createPlaylist(String title, int userId) {
        int playlistId = playlistRepository.createPlaylist(title, userId);
        return playlistRepository.getPlaylistById(playlistId);
    }

    public void setPlaylistCover(String url, int playlistId) {
        playlistRepository.setPlaylistCover(url, playlistId);
    }

    public Playlist setTitle(String title, int playlistId, int userId) {
        boolean updated = playlistRepository.setTitle(title, playlistId, userId);
        if (!updated) {
            return null;
        }

        return playlistRepository.getPlaylistById(playlistId);
    }

    public void addSong(int songId, int playlistId) {
        playlistRepository.addSong(songId, playlistId);
    }

    public Playlist addSong(int songId, int playlistId, int userId) {
        boolean updated = playlistRepository.addSong(songId, playlistId, userId);
        if (!updated) {
            return null;
        }

        return playlistRepository.getPlaylistById(playlistId);
    }

    public void deleteSong(int songId, int playlistId) {
        playlistRepository.deleteSong(songId, playlistId);
    }

    public Playlist deleteSong(int songId, int playlistId, int userId) {
        boolean updated = playlistRepository.deleteSong(songId, playlistId, userId);
        if (!updated) {
            return null;
        }

        return playlistRepository.getPlaylistById(playlistId);
    }

    public boolean deletePlaylist(int playlistId, int userId) {
        return playlistRepository.deletePlaylist(playlistId, userId);
    }

    public Playlist getPlaylistById(int id) {
        return playlistRepository.getPlaylistById(id);
    }
}
