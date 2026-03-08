package musicservice.account;

import musicservice.auth.AuthResponse;
import musicservice.auth.RefreshRequest;
import musicservice.auth.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/sign-up")
    public AuthResponse signUp(@RequestBody SignUpRequest request) {
        return null;
    }

    @PostMapping("/sign-in")
    public AuthResponse signIn(@RequestBody SignInRequest request) {
        return null;
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return null;
    }
}
