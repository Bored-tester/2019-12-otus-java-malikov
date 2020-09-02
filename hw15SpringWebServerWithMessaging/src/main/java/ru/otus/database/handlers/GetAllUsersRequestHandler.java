package ru.otus.database.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.database.core.model.User;
import ru.otus.database.service.DbUserService;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.ResponseMessageGenerator;

import java.util.List;
import java.util.Optional;

public class GetAllUsersRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAllUsersRequestHandler.class);

    private final DbUserService dbUserService;

    public GetAllUsersRequestHandler(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        List<User> users = dbUserService.getAllUsers();
        return Optional.of(ResponseMessageGenerator.produceResponseMessage(msg, users));
    }

}
