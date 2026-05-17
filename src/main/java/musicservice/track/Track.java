package musicservice.track;

import java.util.List;

public record Track (
        int id,
        String title,
        List<Integer> authorIds,
        String album,
        String cover,
        String url
) {
    public Track(int id, String title, int userId, String album, String cover, String url) {
        this(id, title, List.of(userId), album, cover, url);
    }
}
