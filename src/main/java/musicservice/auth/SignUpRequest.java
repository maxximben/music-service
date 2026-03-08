package musicservice.auth;

public record SignUpRequest (
        String email,
        String username,
        String password
) {}
