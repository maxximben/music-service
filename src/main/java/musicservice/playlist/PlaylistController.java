package musicservice.playlist;

import musicservice.account.AccountRepository;
import musicservice.jwt.JwtUtils;
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

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(
            @RequestBody CreatePlaylistRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String title = request.title() == null ? "" : request.title().trim();
        if (title.isEmpty()) {
            return ResponseEntity.badRequest().body("Название плейлиста не может быть пустым");
        }

        int userId = resolveUserId(request, authHeader);
        return ResponseEntity.ok(playlistService.createPlaylist(title, userId));
    }

    private int resolveUserId(CreatePlaylistRequest request, String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return resolveUserId(authHeader);
        }

        if (request.userId() != null) {
            return request.userId();
        }

        throw new IllegalArgumentException("Не удалось определить пользователя");
    }

    private int resolveUserId(String authHeader) {
        String email = jwtUtils.getEmailFromToken(authHeader.substring(7));
        return accountRepository.getIdByEmail(email);
    }

    @PatchMapping("/set-cover")
    public void setCover(@RequestParam("file") MultipartFile file, @RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) throws IOException {
        String url = s3Service.uploadImage(file);
        playlistService.setPlaylistCover(url, playlistId);
    }

    @PatchMapping("/set-title")
    public ResponseEntity<?> setTitle(
            @RequestParam String title,
            @RequestParam int playlistId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        String nextTitle = title == null ? "" : title.trim();
        if (nextTitle.isEmpty()) {
            return ResponseEntity.badRequest().body("Название плейлиста не может быть пустым");
        }

        int userId = resolveUserId(authHeader);
        Playlist playlist = playlistService.setTitle(nextTitle, playlistId, userId);
        if (playlist == null) {
            return ResponseEntity.status(403).body("Плейлист не найден или принадлежит другому пользователю");
        }

        return ResponseEntity.ok(playlist);
    }

    @PatchMapping("/add-song")
    public ResponseEntity<?> addSong(
            @RequestParam int songId,
            @RequestParam int playlistId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        int userId = resolveUserId(authHeader);
        Playlist playlist = playlistService.addSong(songId, playlistId, userId);
        if (playlist == null) {
            return ResponseEntity.status(403).body("Плейлист не найден или принадлежит другому пользователю");
        }

        return ResponseEntity.ok(playlist);
    }

    @PatchMapping("/update")
    public void updatePlaylist(@RequestBody List<Integer> songs) {

    }

    @DeleteMapping("/delete-song")
    public ResponseEntity<?> deleteSong(
            @RequestParam int songId,
            @RequestParam int playlistId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        int userId = resolveUserId(authHeader);
        Playlist playlist = playlistService.deleteSong(songId, playlistId, userId);
        if (playlist == null) {
            return ResponseEntity.status(403).body("Плейлист не найден или принадлежит другому пользователю");
        }

        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePlaylist(
            @RequestParam int playlistId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        int userId = resolveUserId(authHeader);
        boolean deleted = playlistService.deletePlaylist(playlistId, userId);

        if (!deleted) {
            return ResponseEntity.status(403).body("Плейлист не найден или принадлежит другому пользователю");
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getPlaylist(@RequestParam int playlistId, @RequestHeader(value = "Authorization") String authHeader) {
//        String token = authHeader.substring(7);
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }
}
