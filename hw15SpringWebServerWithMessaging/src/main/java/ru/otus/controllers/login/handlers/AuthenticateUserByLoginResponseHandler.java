package ru.otus.controllers.login.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.controllers.login.LoginController;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthenticateUserByLoginResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticateUserByLoginResponseHandler.class);

    private final Map<UUID, Boolean> authenticationResponsesMap;

    public AuthenticateUserByLoginResponseHandler(LoginController loginController) {
        this.authenticationResponsesMap = loginController.getAuthenticationResponsesMap();
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            boolean authResponse = Serializers.deserialize(msg.getPayload(), Boolean.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            authenticationResponsesMap.put(sourceMessageId, authResponse);
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
