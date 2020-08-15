package ru.otus.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbUserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserCreationController {

    private final DbUserService dbUserService;
    private final InputUserParser inputUserParser;

    public UserCreationController(DbUserService dbUserService, InputUserParser inputUserParser) {
        this.dbUserService = dbUserService;
        this.inputUserParser = inputUserParser;
    }

    @PostMapping("/api/user/create")
    public User userCreateView(HttpServletRequest request) throws IOException {
        String unparsedUser = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = inputUserParser.parseInputUser(unparsedUser);
        dbUserService.saveUser(user);
        return user;
    }

}
