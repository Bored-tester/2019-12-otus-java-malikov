package ru.otus.controllers.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbUserService;

@RestController
public class UserCreationController {

    private final DbUserService dbUserService;

    public UserCreationController(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @PostMapping("/api/user/create")
    public User userCreate(@RequestBody User user) {
        dbUserService.saveUser(user);
        return user;
    }

}
