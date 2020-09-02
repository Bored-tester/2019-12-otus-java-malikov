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

public class GetUserByIdRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserByIdRequestHandler.class);

    private final DbUserService dbUserService;

    public GetUserByIdRequestHandler(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        long id = Serializers.deserialize(msg.getPayload(), Long.class);
        User user = dbUserService.getUser(id).orElse(null);
        return Optional.of(ResponseMessageGenerator.produceResponseMessage(msg, user));
    }
}
