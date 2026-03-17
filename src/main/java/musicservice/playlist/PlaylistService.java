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



}
