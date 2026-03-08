package musicservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import musicservice.auth.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final int accessTokenExpiration;
    private final int refreshTokenExpiration;

    public JwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration}") int accessTokenExpiration,
            @Value("${jwt.refresh-expiration}") int refreshTokenExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateToken(String email, char type) {

        int exp = 0;

        if (type == 'a') {
            exp = accessTokenExpiration;
        } else if (type == 'r') {
            exp = refreshTokenExpiration;
        }

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key)
                .compact();
    }


    public AuthResponse generateTokens(String email) {
        String access = generateToken(email, 'a');
        String refresh = generateToken(email, 'r');

        return new AuthResponse(access, refresh);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
