package musicservice.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    SearchRepository searchRepository;

    @GetMapping("/search")
    public SearchResult search(@RequestParam String request) {
        return searchRepository.findItemsByName(request);
    }
}
