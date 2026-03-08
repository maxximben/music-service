package musicservice.playlist;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/playlist")
public class PlaylistController {

    @PostMapping("/create")
    public void createPlaylist(@RequestBody List<Integer> songs) {

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
