package musicservice.auth;

public record SignInRequest (
        String email,
        String password
) {}
