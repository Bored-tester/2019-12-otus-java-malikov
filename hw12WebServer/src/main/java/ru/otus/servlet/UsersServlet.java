package ru.otus.servlet;

import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.model.User;
import ru.otus.database.core.sessionmanager.SessionManager;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            List<User> allUsers = userDao.getAll().orElse(null);

            response.setContentType("text/html");
            Map<String, Object> usersMap = new HashMap<>();
            usersMap.put(TEMPLATE_ATTR_USERS, allUsers);

                try {
                    response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, usersMap));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

}
