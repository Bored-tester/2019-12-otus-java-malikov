package ru.otus.database.hibernate.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.dao.UserDaoException;
import ru.otus.database.core.model.User;
import ru.otus.database.core.sessionmanager.SessionManager;
import ru.otus.database.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.database.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoHibernate implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        try (sessionManager) {
            sessionManager.beginSession();
            DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
            try {
                return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (sessionManager) {
            sessionManager.beginSession();
            DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
            try {
                return Optional.ofNullable((User) currentSession
                        .getHibernateSession()
                        .createQuery("from User where login =:login")
                        .setParameter("login", login)
                        .uniqueResult());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        try (sessionManager) {
            sessionManager.beginSession();
            DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
            try {
                return currentSession.getHibernateSession().createQuery("from User", User.class).getResultList();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return Collections.emptyList();
        }
    }


    @Override
    public long saveUser(User user) {
        try (sessionManager) {
            sessionManager.beginSession();
            DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
            try {
                Session hibernateSession = currentSession.getHibernateSession();
                if (user.getId() > 0) {
                    hibernateSession.merge(user);
                } else {
                    hibernateSession.persist(user);
                }
                return user.getId();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new UserDaoException(e);
            }
        }

    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
