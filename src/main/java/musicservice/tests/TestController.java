package musicservice.tests;

import musicservice.user.User;
import musicservice.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/userSaveTest")
    public void savaUser(@RequestBody User user) {
        accountService.save(user);
    }

    @GetMapping("findAll")
    public List<User> findAll() {
        return accountService.findAll();
    }
}
