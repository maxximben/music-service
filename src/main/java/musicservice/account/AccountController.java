package musicservice.account;

import musicservice.auth.*;
import musicservice.jwt.JwtUtils;
import musicservice.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        accountService.save(request);
        return ResponseEntity.ok(jwtUtils.generateTokens(request.email()));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {

        User user = accountRepository.findByEmail(request.email());

        if (encoder.matches(request.password(), user.password())) {
            return ResponseEntity.ok(jwtUtils.generateTokens(request.email()));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("Неверные данные"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

        String token = request.refreshToken();

        if (jwtUtils.validateToken(token)) {
            String email = jwtUtils.getEmailFromToken(token);
            return ResponseEntity.ok(jwtUtils.generateTokens(email));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Невалидый токен"));
    }
}
