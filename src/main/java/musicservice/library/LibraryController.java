package musicservice.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

    @Autowired
    LibraryRepository libraryRepository;

    @GetMapping("/library")
    public Library getLibrary(@RequestParam int userId,  @RequestHeader(value = "Authorization") String authHeader) {
        return libraryRepository.findItemsByUserId(userId);
    }
}
