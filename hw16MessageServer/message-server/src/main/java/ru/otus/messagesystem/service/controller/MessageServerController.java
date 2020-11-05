package ru.otus.messagesystem.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.message.model.MessageClientId;
import ru.otus.messagesystem.rest.uri.MsServerUriDictionary;
import ru.otus.messagesystem.service.MessageSystem;

@RestController
public class MessageServerController {

    private static final Logger logger = LoggerFactory.getLogger(MessageServerController.class);

    private final MessageSystem messageSystem;

    public MessageServerController(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    @PostMapping(MsServerUriDictionary.API_SERVER_MESSAGE_SEND)
    public String sendMessage(@RequestBody Message message) {
        boolean result = messageSystem.newMessage(message);
        if (!result) {
            return String.format("The last message was rejected: %s", message.toString());
        }
        return String.format("Message from %s to %s on subject %s was successfully sent!",
                message.getFrom(),
                message.getTo(),
                message.getType());
    }

    @PostMapping(MsServerUriDictionary.API_SERVER_REGISTER)
    public void registerClient(@RequestBody MessageClientId messageClientId) {
        messageSystem.addClient(messageClientId);
    }

}
