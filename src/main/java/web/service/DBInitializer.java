package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;

import javax.annotation.PostConstruct;
import java.util.Set;

@Service
public class DBInitializer {

    @Autowired
    private UserService userService;

    @Transactional
    @PostConstruct
    public void initDB() {

        userService.addUser(new User("admin", "admin", (byte) 45, "admin@test.com",
                "admin", Set.of(new Role("ADMIN"))));
        userService.addUser(new User( "user", "user", (byte) 25, "user@test.com",
                "user", Set.of(new Role("USER"))));
    }
}