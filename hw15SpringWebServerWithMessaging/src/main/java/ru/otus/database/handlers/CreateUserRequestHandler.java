package ru.otus.database.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.database.core.model.User;
import ru.otus.database.service.DbUserService;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;

import java.util.Optional;

public class CreateUserRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserRequestHandler.class);

    private final DbUserService dbUserService;

    public CreateUserRequestHandler(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        User user = Serializers.deserialize(msg.getPayload(), User.class);
        dbUserService.saveUser(user);
        return Optional.empty();
    }
}
