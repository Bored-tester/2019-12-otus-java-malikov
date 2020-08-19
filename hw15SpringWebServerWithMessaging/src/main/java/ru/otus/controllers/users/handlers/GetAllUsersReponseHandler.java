package ru.otus.controllers.users.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.app.common.Serializers;
import ru.otus.controllers.users.UserApiController;
import ru.otus.controllers.users.UserPageController;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.RequestHandler;
import ru.otus.messagesystem.message.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetAllUsersReponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAllUsersReponseHandler.class);

    private final SimpMessagingTemplate template;

    public GetAllUsersReponseHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            List<User> userData = Serializers.deserialize(msg.getPayload(), List.class);
//            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
//            userApiController.getUsersResponseMap().put(sourceMessageId, userData);
            this.template.convertAndSend("/topic/user/list", userData);

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
