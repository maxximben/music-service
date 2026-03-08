package musicservice.track;

public record Track (
        int id,
        String title,
        int userId,
        String album,
        String cover,
        String url
) {}
