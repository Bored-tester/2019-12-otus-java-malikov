package ru.otus.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbUserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final DbUserService dbUserService;
    private final InputUserParser inputUserParser;

    public UserController(DbUserService dbUserService, InputUserParser inputUserParser) {
        this.dbUserService = dbUserService;
        this.inputUserParser = inputUserParser;
    }

    @PostMapping("/api/user/create")
    public String userCreateView(HttpServletRequest request) throws IOException {
        String unparsedUser = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = inputUserParser.parseIputUser(unparsedUser);
        dbUserService.saveUser(user);
        return USERS_PAGE_TEMPLATE;
    }

    @GetMapping("/users")
    protected String getAllUsers(Model model) {
        List<User> allUsers = dbUserService.getAllUsers();

        model.addAttribute(TEMPLATE_ATTR_USERS, allUsers);
        return USERS_PAGE_TEMPLATE;
    }

}
