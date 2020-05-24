package ru.otus.servlet.api.user;

import com.google.gson.Gson;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.dao.UserDaoException;
import ru.otus.database.core.model.User;
import ru.otus.database.core.sessionmanager.SessionManager;
import ru.otus.database.core.sessionmanager.SessionManagerException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


public class UsersApiServlet extends HttpServlet {

    private final UserDao userDao;
    private final Gson gson;

    public UsersApiServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!request.getPathInfo().equals("/create")) {
            System.out.println(String.format("ERROR! Incorrect url:\n%s", request.getRequestURI()));
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        String unparsedUser = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            User user = InputUserParser.parseIputUser(unparsedUser);
            try (SessionManager sessionManager = userDao.getSessionManager()) {
                sessionManager.beginSession();
                userDao.saveUser(user);
            } catch (SessionManagerException e) {
                throw new UserDaoException(e);
            }

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(user));
        } catch (IllegalArgumentException e) {
            System.out.println(String.format("ERROR! Failed to parse user \n%s\nProbably invalid role value. Should be ADMIN or MORTAL.", unparsedUser));
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
