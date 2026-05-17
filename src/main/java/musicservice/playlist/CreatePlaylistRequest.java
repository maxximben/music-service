package musicservice.playlist;

public record CreatePlaylistRequest (
        String title,
        Integer userId
) {}
