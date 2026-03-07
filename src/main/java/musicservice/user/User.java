package musicservice.user;

public record User (
        String username,
        String avatar,
        String password,
        boolean isArtist,
        String email
) {}
