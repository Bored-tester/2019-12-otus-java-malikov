package ru.otus.core.dao;

import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface EntityDao<T> {
    Optional<T> findEntityById(Class<T> clazz, long id);

    long saveEntity(T entity);

    SessionManager getSessionManager();
}
