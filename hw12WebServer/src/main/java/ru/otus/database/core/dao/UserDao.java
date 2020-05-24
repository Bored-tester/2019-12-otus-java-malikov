package ru.otus.database.core.dao;

import ru.otus.database.core.model.User;
import ru.otus.database.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    Optional<List<User>> getAll();

    long saveUser(User user);

    SessionManager getSessionManager();
}
