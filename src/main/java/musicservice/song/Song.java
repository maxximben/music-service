package musicservice.song;

import java.util.List;

public record Song (
        int songId,
        String title,
        String cover,
        String url,
        Integer duration,
        String author,
        List<String> authors
) {}
