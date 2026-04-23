package musicservice.recs;

import musicservice.playlist.Playlist;
import musicservice.playlist.PlaylistRepository;
import musicservice.song.SongRepository;
import musicservice.webhook.SimilarSongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecsController {

    @Autowired
    SimilarSongsService similarSongsService;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    SongRepository songRepository;

    @GetMapping("/get-radio")
    public Playlist getRadio(int songId, int userId) {
        String title = songRepository.getSongById(songId).title() + " (radio)";
        List<Integer> songs = similarSongsService.getTop20SimilarSongIds(songId);
        int playlistId = playlistRepository.createPlaylistWithSongs(title, userId, songs);
        return playlistRepository.getPlaylistById(playlistId);
    }



}
