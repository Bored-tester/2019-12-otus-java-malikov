package ru.otus.controllers.users.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.controllers.users.UserController;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GetAllUsersReponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAllUsersReponseHandler.class);

    private final UserController userController;

    public GetAllUsersReponseHandler(UserController userController) {
        this.userController = userController;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            List<User> userData = Serializers.deserialize(msg.getPayload(), List.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            userController.getUsersResponseMap().put(sourceMessageId, userData);
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
