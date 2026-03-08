package musicservice.account;

public record SignInRequest (
        String email,
        String password
) {}
