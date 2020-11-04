package ru.otus.authentication.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.app.common.Waiter;
import ru.otus.authentication.UserAuthService;
import ru.otus.controllers.login.model.AuthUserData;
import ru.otus.database.core.enums.UserRole;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.ResponseMessageGenerator;
import ru.otus.messagesystem.message.enums.MessageType;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthenticateUserByLoginRequestHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticateUserByLoginRequestHandler.class);
    private static final int GET_USER_TIMEOUT_IN_SECONDS = 10;

    private final MsClient authMessageClient;
    private final Map<UUID, User> userResponsesMap;

    public AuthenticateUserByLoginRequestHandler(UserAuthService userAuthService, MsClient authMessageClient, Map<UUID, User> userResponsesMap) {
        this.authMessageClient = authMessageClient;
        this.userResponsesMap = userResponsesMap;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);

        AuthUserData userData = Serializers.deserialize(msg.getPayload(), AuthUserData.class);
        Message getUserByLoginMessage = authMessageClient.produceMessage(ClientNameDictionary.DATABASE_SERVICE_CLIENT_NAME.getName(), userData.getLogin(), MessageType.GET_USER_DATA_BY_LOGIN);
        authMessageClient.sendMessage(getUserByLoginMessage);

        Optional<User> user = Waiter.waitForMessagePayloadInMap(getUserByLoginMessage.getId(), userResponsesMap, GET_USER_TIMEOUT_IN_SECONDS);
        boolean isAuthenticated = user
                .map(loggingInUser -> (loggingInUser.getPassword().equals(userData.getPassword())) && (loggingInUser.getRole().equals(UserRole.ADMIN)))
                .orElse(false);
        return Optional.of(ResponseMessageGenerator.produceResponseMessage(msg, isAuthenticated));
    }
}
