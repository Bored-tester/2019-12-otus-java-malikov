package ru.otus.services;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.enums.UserRole;
import ru.otus.database.core.sessionmanager.SessionManager;

@Service
@AllArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    @Override
    public boolean authenticate(String login, String password) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            return userDao.findByLogin(login)
                    .map(user -> (user.getPassword().equals(password)) && (user.getRole().equals(UserRole.ADMIN)))
                    .orElse(false);
        }
    }

}
