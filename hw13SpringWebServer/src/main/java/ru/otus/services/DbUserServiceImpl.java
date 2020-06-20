package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbServiceException;
import ru.otus.database.core.service.DbUserService;

import java.util.List;
import java.util.Optional;

@Repository
public class DbUserServiceImpl implements DbUserService {
    private static Logger logger = LoggerFactory.getLogger(DbUserServiceImpl.class);

    private final UserDao userDao;

    public DbUserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try {
            long userId = userDao.saveUser(user);

            logger.info("created user: {}", userId);
            return userId;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DbServiceException(e);
        }
    }


    @Override
    public Optional<User> getUser(long id) {
        try {
            Optional<User> userOptional = userDao.findById(id);

            logger.info("user: {}", userOptional.orElse(null));
            return userOptional;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

}
