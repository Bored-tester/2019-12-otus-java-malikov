package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.services.UserAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 60;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";

    private final UserAuthService userAuthService;

    public LoginController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/login")
    protected String doGet() {
        return LOGIN_PAGE_TEMPLATE;
    }

    @PostMapping("/login")
    protected RedirectView doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            return new RedirectView("/users", true);
        } else {
            return new RedirectView("/login", true);
        }

    }

}
