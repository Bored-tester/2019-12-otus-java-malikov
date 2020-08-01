package ru.otus.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.model.User;
import ru.otus.database.handlers.CreateUserRequestHandler;
import ru.otus.database.handlers.GetAllUsersRequestHandler;
import ru.otus.database.handlers.GetUserByIdRequestHandler;
import ru.otus.database.handlers.GetUserByLoginRequestHandler;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.service.MessageSystem;

import java.util.List;
import java.util.Optional;

@Repository
public class DbUserServiceImpl implements DbUserService {
    private static Logger logger = LoggerFactory.getLogger(DbUserServiceImpl.class);

    private final UserDao userDao;
    private final MsClient messageClient;

    public DbUserServiceImpl(UserDao userDao, MessageSystem messageSystem) {
        this.userDao = userDao;
        messageClient = new MsClientImpl(ClientNameDictionary.DATABASE_SERVICE_CLIENT_NAME.getName(), messageSystem);
        messageClient.addHandler(MessageType.GET_USERS_DATA, new GetAllUsersRequestHandler(this));
        messageClient.addHandler(MessageType.GET_USER_DATA_BY_ID, new GetUserByIdRequestHandler(this));
        messageClient.addHandler(MessageType.GET_USER_DATA_BY_LOGIN, new GetUserByLoginRequestHandler(this));
        messageClient.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(this));
        messageSystem.addClient(messageClient);
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
    public Optional<User> getUser(String login) {
        try {
            Optional<User> userOptional = userDao.findByLogin(login);

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
