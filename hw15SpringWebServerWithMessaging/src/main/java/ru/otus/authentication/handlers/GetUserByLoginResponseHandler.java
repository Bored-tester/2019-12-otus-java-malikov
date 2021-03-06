package ru.otus.authentication.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.authentication.UserAuthService;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GetUserByLoginResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserByLoginResponseHandler.class);

    private final Map<UUID, User> userResponsesMap;

    public GetUserByLoginResponseHandler(UserAuthService userAuthService, Map<UUID, User> userResponsesMap) {
        this.userResponsesMap = userResponsesMap;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            User user = Serializers.deserialize(msg.getPayload(), User.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            userResponsesMap.put(sourceMessageId, user);
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
