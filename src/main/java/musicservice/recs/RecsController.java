package musicservice.recs;

import musicservice.account.AccountRepository;
import musicservice.jwt.JwtUtils;
import musicservice.playlist.Playlist;
import musicservice.playlist.PlaylistRepository;
import musicservice.song.SongRepository;
import musicservice.webhook.SimilarSongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecsController {

    @Autowired
    SimilarSongsService similarSongsService;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/get-radio")
    public Playlist createRadio(@RequestParam int songId, @RequestHeader(value = "Authorization") String authHeader) {
        return createRadioPlaylist(songId, resolveUserId(authHeader));
    }

    @GetMapping("/get-radio")
    public Playlist getRadio(@RequestParam int songId, @RequestParam int userId) {
        return createRadioPlaylist(songId, userId);
    }

    private Playlist createRadioPlaylist(int songId, int userId) {
        String title = songRepository.getSongById(songId).title() + " (radio)";
        List<Integer> songs = new ArrayList<>();
        songs.add(songId);
        songs.addAll(similarSongsService.getTop20SimilarSongIds(songId));
        int playlistId = playlistRepository.createPlaylistWithSongs(title, userId, songs);
        return playlistRepository.getPlaylistById(playlistId);
    }

    private int resolveUserId(String authHeader) {
        String email = jwtUtils.getEmailFromToken(authHeader.substring(7));
        return accountRepository.getIdByEmail(email);
    }
}
