package musicservice.search;

import musicservice.account.AccountRepository;
import musicservice.history.HistoryRepository;
import musicservice.jwt.JwtUtils;
import musicservice.play.PlayController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    PlayController playController;

    @GetMapping("/search")
    public SearchResult search(@RequestParam String request, @RequestHeader(value = "Authorization") String authHeader) {
        return searchRepository.findItemsByName(request);
    }


    @GetMapping("/search/play")
    public String play(@RequestParam int songId, @RequestHeader(value = "Authorization") String authHeader) {

        int userId = accountRepository.getIdByEmail(
                jwtUtils.getEmailFromToken(authHeader.substring(7))
        );

        historyRepository.savePlayedSong(userId, songId);

        return playController.play(songId);
    }
}
