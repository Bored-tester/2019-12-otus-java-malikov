package ru.otus.controllers.user;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.model.User;
import ru.otus.controllers.user.InputUserParser;
import ru.otus.database.core.sessionmanager.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserController {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final UserDao userDao;
    private final InputUserParser inputUserParser;
    private final Gson gson;


//    public CreateUserController(UserDao userDao) {
//        this.userDao = userDao;
//    }

    @PostMapping("/api/user/create")
    public String userCreateView(HttpServletRequest request) throws IOException {
        String unparsedUser = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

//        try {
            User user = inputUserParser.parseIputUser(unparsedUser);
        userDao.saveUser(user);

//        try (SessionManager sessionManager = userDao.getSessionManager()) {
//                sessionManager.beginSession();
//                userDao.saveUser(user);
//            } catch (SessionManagerException e) {
//                throw new UserDaoException(e);
//            }

//            response.setContentType("application/json;charset=UTF-8");
//            ServletOutputStream out = response.getOutputStream();
//            out.print(gson.toJson(user));
//        } catch (IllegalArgumentException e) {
//            System.out.println(String.format("ERROR! Failed to parse user \n%s\nProbably invalid role value. Should be ADMIN or MORTAL.", unparsedUser));
//            response.setContentType("application/json;charset=UTF-8");
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
        return "users.html";
    }

    @GetMapping("/users")
    protected String getAllUsers(Model model) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            List<User> allUsers = userDao.getAll();

//            response.setContentType("text/html");
//            Map<String, Object> usersMap = new HashMap<>();
//            usersMap.put(TEMPLATE_ATTR_USERS, allUsers);

            model.addAttribute(TEMPLATE_ATTR_USERS, allUsers);
            return USERS_PAGE_TEMPLATE;
//            try {
//                response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, usersMap));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

//    @PostMapping("/user/save")
//    public RedirectView userSave(@ModelAttribute User user) {
//        repository.create(user.getName());
//        return new RedirectView("/user/list", true);
//    }

}
