package musicservice.account;

import musicservice.auth.SignUpRequest;
import musicservice.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void save(SignUpRequest request) {
        accountRepository.save(request);
    }

    public List<User> findAll() {
        return accountRepository.findAll();
    }
}
