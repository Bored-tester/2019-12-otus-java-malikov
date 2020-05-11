package ru.otus.jdbc.dao;


import ru.otus.core.dao.EntityDao;
import ru.otus.core.dao.EntityDaoException;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sqlmapper.SqlMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.util.Optional;

public class EntityDaoJdbc<T> implements EntityDao<T> {

    private final SessionManagerJdbc sessionManager;
    private final SqlMapper<T> sqlMapper;

    public EntityDaoJdbc(SessionManagerJdbc sessionManager, SqlMapper<T> sqlMapper) {
        this.sessionManager = sessionManager;
        this.sqlMapper = sqlMapper;
    }


    @Override
    public Optional<T> findEntityById(Class<T> clazz, long id) {
        try {
            return sqlMapper.selectRecord(getConnection(), clazz, id);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e);
        }
        return Optional.empty();
    }


    @Override
    public long saveEntity(T entity) {
        try {
            return sqlMapper.insertRecord(getConnection(), entity);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e);
            throw new EntityDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
