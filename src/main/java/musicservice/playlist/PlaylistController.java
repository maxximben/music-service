package musicservice.playlist;

import musicservice.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    S3Service s3Service;

    @PostMapping("/create")
    public void createPlaylist(@RequestBody CreatePlaylistRequest request) {
        playlistService.createPlaylist(request.title(), request.userId());
    }

    @PatchMapping("/set-cover")
    public void setCover(@RequestParam("file") MultipartFile file, @RequestParam int playlistId) throws IOException {
        String url = s3Service.uploadImage(file);
        playlistService.setPlaylistCover(url, playlistId);
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
