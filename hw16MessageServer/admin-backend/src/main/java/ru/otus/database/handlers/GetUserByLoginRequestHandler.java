package ru.otus.database.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.database.core.model.User;
import ru.otus.database.service.DbUserService;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.message.ResponseMessageGenerator;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.message.model.Message;

import java.util.Optional;

@Component
public class GetUserByLoginRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserByLoginRequestHandler.class);

    private final DbUserService dbUserService;

    public GetUserByLoginRequestHandler(DbUserService dbUserService, MsClient messageClient) {
        this.dbUserService = dbUserService;
        messageClient.addHandler(MessageType.GET_USER_DATA_BY_LOGIN, this);
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        String login = Serializers.deserializeFromJson(msg.getPayload(), String.class);
        User user = dbUserService.getUser(login).orElse(null);
        return Optional.of(ResponseMessageGenerator.produceResponseMessage(msg, user));
    }
}
