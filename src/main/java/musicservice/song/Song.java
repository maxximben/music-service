package musicservice.song;

public record Song (
        int songId,
        String title,
        int userId,
        String cover,
        String url,
        int duration,
        String author
) {}
