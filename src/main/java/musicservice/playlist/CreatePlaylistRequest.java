package musicservice.playlist;

public record CreatePlaylistRequest (
        String title,
        int userId
) {}
