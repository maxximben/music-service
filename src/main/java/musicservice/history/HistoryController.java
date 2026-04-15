package musicservice.history;

import musicservice.account.AccountRepository;
import musicservice.jwt.JwtUtils;
import musicservice.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/history")
    public SearchResult getHistory(@RequestHeader(value = "Authorization") String authHeader) {
        int userId = accountRepository.getIdByEmail(
                jwtUtils.getEmailFromToken(authHeader.substring(7))
        );

        return historyRepository.getSearchHistoryByUserId(userId);
    }
}