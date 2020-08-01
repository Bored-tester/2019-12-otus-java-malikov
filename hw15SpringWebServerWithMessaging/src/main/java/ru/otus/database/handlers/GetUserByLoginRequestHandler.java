package ru.otus.database.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.database.core.model.User;
import ru.otus.database.service.DbUserService;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.ResponseMessageGenerator;

import java.util.Optional;

public class GetUserByLoginRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserByLoginRequestHandler.class);

    private final DbUserService dbUserService;

    public GetUserByLoginRequestHandler(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        String login = Serializers.deserialize(msg.getPayload(), String.class);
        User user = dbUserService.getUser(login).orElse(null);
        return Optional.of(ResponseMessageGenerator.produceResponseMessage(msg, user));
    }
}
