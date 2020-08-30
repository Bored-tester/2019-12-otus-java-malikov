package ru.otus.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbUserService;

import java.util.List;

@Controller
public class UsersPageController {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final DbUserService dbUserService;

    public UsersPageController(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @GetMapping("/users")
    protected String getAllUsers(Model model) {
        List<User> allUsers = dbUserService.getAllUsers();

        model.addAttribute(TEMPLATE_ATTR_USERS, allUsers);
        return USERS_PAGE_TEMPLATE;
    }

}
