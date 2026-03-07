package musicservice.account;

import musicservice.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String query = "insert into users (username, avatar, password, is_artist, email) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, user.username(), user.avatar(), user.password(), user.isArtist(), user.email());
    }

    public List<User> findAll() {
        String query = "select * from users";
        return jdbcTemplate.query(query, userRawMapper());
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
