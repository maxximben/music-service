package musicservice.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    @Autowired
    PlaylistRepository playlistRepository;

    public void createPlaylist(String title, int userId) {
        playlistRepository.createPlaylist(title, userId);
    }

    public void setPlaylistCover(String url, int playlistId) {
        playlistRepository.setPlaylistCover(url, playlistId);
    }

    public void setTitle(String title, int playlistId) {
        playlistRepository.setTitle(title, playlistId);
    }


}
