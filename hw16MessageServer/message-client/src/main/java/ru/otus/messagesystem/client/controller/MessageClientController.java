package ru.otus.messagesystem.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.rest.uri.MsClientUriDictionary;

@RestController
public class MessageClientController {

    private static final Logger logger = LoggerFactory.getLogger(MessageClientController.class);

    private final MsClient messageClient;

    public MessageClientController(MsClient messageClient) {
        this.messageClient = messageClient;
    }

    @PostMapping(MsClientUriDictionary.API_CLIENT_MESSAGE_SEND)
    public String handleMessage(@RequestBody Message message) {
        logger.info("Message {}\nreceived and will be sent to responsible handler.", message);
        messageClient.handle(message);
        return String.format("Message from %s to %s on subject %s was successfully received and handled!",
                message.getFrom(),
                message.getTo(),
                message.getType());
    }

}
