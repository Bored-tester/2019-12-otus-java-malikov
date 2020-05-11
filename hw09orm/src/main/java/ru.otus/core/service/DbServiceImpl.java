package ru.otus.core.service;

import ru.otus.core.dao.EntityDao;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {

    private final EntityDao entityDao;

    public DbServiceImpl(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    @Override
    public long saveEntityToDb(T user) {
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = entityDao.saveEntity(user);
                sessionManager.commitSession();

                System.out.println(String.format("created user: %d", userId));
                return userId;
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n" + e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<T> getEntityById(Class<T> clazz, long id) {
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<T> userOptional = entityDao.findEntityById(clazz, id);

                System.out.println(String.format("user: %s", userOptional.orElse(null)));
                return userOptional;
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n" + e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

}
