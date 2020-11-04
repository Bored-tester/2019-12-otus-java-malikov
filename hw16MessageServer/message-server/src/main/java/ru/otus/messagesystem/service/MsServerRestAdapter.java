package ru.otus.messagesystem.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.rest.RestClient;
import ru.otus.messagesystem.rest.uri.MsClientUriDictionary;

@Component
public class MsServerRestAdapter {
    private final Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);

    public void sendMessageToClient(Message message) {
        RestClient restClient = new RestClient(message.getTo().getSocket());
        String json = gson.toJson(message);
        ResponseEntity<String> responseEntity = restClient.post(MsClientUriDictionary.API_CLIENT_MESSAGE_SEND, json);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            logger.error("Code {} received! Message to client failed {}", responseEntity.getStatusCode(), message);
        }
    }
}
