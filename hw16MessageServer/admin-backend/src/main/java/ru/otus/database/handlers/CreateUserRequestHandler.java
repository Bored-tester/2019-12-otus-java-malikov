package ru.otus.database.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.database.core.model.User;
import ru.otus.database.service.DbUserService;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.message.model.Message;

import java.util.Optional;

@Component
public class CreateUserRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserRequestHandler.class);

    private final DbUserService dbUserService;

    public CreateUserRequestHandler(DbUserService dbUserService, MsClient messageClient) {
        this.dbUserService = dbUserService;
        messageClient.addHandler(MessageType.CREATE_USER, this);
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        User user = Serializers.deserializeFromJson(msg.getPayload(), User.class);
        dbUserService.saveUser(user);
        return Optional.empty();
    }
}
