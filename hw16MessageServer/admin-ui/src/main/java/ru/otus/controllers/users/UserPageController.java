package ru.otus.controllers.users;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.common.model.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class UserPageController {

    private static final String USERS_PAGE_TEMPLATE = "users.html";

    @Getter
    private final Map<UUID, List<User>> usersResponseMap = new ConcurrentHashMap<>();

    @GetMapping("/users")
    protected String getUsersAdminPage(Model model) {
        return USERS_PAGE_TEMPLATE;
    }
}
