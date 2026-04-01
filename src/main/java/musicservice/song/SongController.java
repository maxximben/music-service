package musicservice.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/song")
    public ResponseEntity<?> getSong(@RequestParam int id) {
        return ResponseEntity.ok(songRepository.getSongById(id));
    }

}
