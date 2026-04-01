package musicservice.playlist;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import musicservice.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public void setCover(@RequestParam("file") MultipartFile file, @RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) throws IOException {
        String url = s3Service.uploadImage(file);
        playlistService.setPlaylistCover(url, playlistId);
    }

    @PatchMapping("/set-title")
    public void setTitle(@RequestParam String title, @RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) {
        playlistService.setTitle(title, playlistId);
    }

    @PatchMapping("/add-song")
    public void addSong(@RequestParam int songId, @RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) {
        playlistService.addSong(songId, playlistId);
    }

    @PatchMapping("/update")
    public void updatePlaylist(@RequestBody List<Integer> songs) {

    }

    @DeleteMapping("/delete-song")
    public void deletePlaylist(@RequestParam int songId, @RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) {

        String token = authHeader.substring(7);   // доделать

        playlistService.deleteSong(songId, playlistId);
    }

    @GetMapping
    public ResponseEntity<?> getPlaylist(@RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) {
//        String token = authHeader.substring(7);
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }
}
