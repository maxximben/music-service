package musicservice.playlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    PlaylistService playlistService;

    @PostMapping("/create")
    public void createPlaylist(@RequestBody CreatePlaylistRequest request) {
        playlistService.createPlaylist(request.title(), request.userId());
    }

    @PostMapping("/add")
    public void addSong(@RequestParam int id) {

    }

    @PatchMapping("/update")
    public void updatePlaylist(@RequestBody List<Integer> songs) {

    }

    @DeleteMapping("/delete")
    public void deletePlaylist(@RequestParam int id) {

    }
}
