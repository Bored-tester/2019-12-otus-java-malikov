package ru.otus.controllers.users.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.otus.common.model.User;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.message.model.Message;

import java.util.List;
import java.util.Optional;

@Component
public class GetAllUsersReponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAllUsersReponseHandler.class);
    private static final String USER_LIST_WEB_SOCKET = "/topic/user/list";

    private final SimpMessagingTemplate simpMessagingTemplate;

    public GetAllUsersReponseHandler(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            List<User> userData = Serializers.deserializeFromJson(msg.getPayload(), List.class);
            this.simpMessagingTemplate.convertAndSend(USER_LIST_WEB_SOCKET, userData);

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
