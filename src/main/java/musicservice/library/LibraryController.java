package musicservice.library;

import musicservice.account.AccountRepository;
import musicservice.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
        int userId = accountRepository.getIdByEmail(
                jwtUtils.getEmailFromToken(authHeader.substring(7))
        );

        return libraryRepository.findItemsByUserId(userId);
    }
}
