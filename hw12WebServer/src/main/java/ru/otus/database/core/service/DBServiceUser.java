package ru.otus.database.core.service;

import ru.otus.database.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}
