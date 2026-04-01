package musicservice.account;

import musicservice.auth.SignUpRequest;
import musicservice.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Repository
public class AccountRepository {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(SignUpRequest request) {
        String query = "insert into users (username, password, is_artist, email) values (?, ?, ?, ?)";

        String password = request.password();

        String hashed = passwordEncoder.encode(password);

        jdbcTemplate.update(query, request.username(), hashed, false, request.email());
    }

    public List<User> findAll() {
        String query = "select * from users";
        return jdbcTemplate.query(query, userRawMapper());
    }

    public User findByEmail(String email) {
        String query = "select * from users where email = ?";
        return jdbcTemplate.queryForObject(query, userRawMapper(), email);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email));
    }

    private RowMapper<User> userRawMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("username"),
                rs.getString("avatar"),
                rs.getString("password"),
                rs.getBoolean("is_artist"),
                rs.getString("email")
        );
    }
}
