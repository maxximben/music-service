package musicservice.library;

import musicservice.account.AccountRepository;
import musicservice.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/library")
    public Library getLibrary(@RequestHeader(value = "Authorization") String authHeader) {
        int userId = resolveUserId(authHeader);
        return libraryRepository.findItemsByUserId(userId);
    }

    @PostMapping("/library/add-album")
    public ResponseEntity<?> addAlbumToLibrary(
            @RequestParam int albumId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        int userId = resolveUserId(authHeader);

        if (!libraryRepository.albumExists(albumId)) {
            return ResponseEntity.notFound().build();
        }

        libraryRepository.addAlbumToLibrary(userId, albumId);
        return ResponseEntity.ok(libraryRepository.findItemsByUserId(userId));
    }

    private int resolveUserId(String authHeader) {
        int userId = accountRepository.getIdByEmail(
                jwtUtils.getEmailFromToken(authHeader.substring(7))
        );
        return userId;
    }
}
