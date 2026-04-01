package musicservice.webhook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SimilarSongsTestController {

    private final SimilarSongsService similarSongsService;

    public SimilarSongsTestController(SimilarSongsService similarSongsService) {
        this.similarSongsService = similarSongsService;
    }

    @GetMapping("/test/similar-songs/ids/{songId}")
    public List<Integer> getSimilarSongIds(@PathVariable int songId) {
        return similarSongsService.getTop20SimilarSongIds(songId);
    }

    @GetMapping("/test/similar-songs/titles/{songId}")
    public List<String> getSimilarSongTitles(@PathVariable int songId) {
        return similarSongsService.getTop20SimilarSongTitles(songId);
    }
}