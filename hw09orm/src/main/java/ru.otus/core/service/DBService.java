package ru.otus.core.service;

import java.util.Optional;

public interface DBService<T> {

    long saveEntityToDb(T user);

    Optional<T> getEntityById(Class<T> clazz, long id);

}
