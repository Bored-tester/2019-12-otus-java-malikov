package ru.otus.database.service;

import ru.otus.database.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DbUserService {

    long saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUser(String login);

    List<User> getAllUsers();

}
