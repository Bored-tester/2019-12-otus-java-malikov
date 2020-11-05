package ru.otus.messagesystem.client;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.message.model.MessageClientId;
import ru.otus.messagesystem.rest.RestClient;
import ru.otus.messagesystem.rest.uri.MsServerUriDictionary;

@Component
public class MsClientRestAdapter {
    public static final String SERVER_PORT = "8083";
    private static final String MESSAGE_SERVER_SOCKET = "http://localhost:" + SERVER_PORT;
    private static final RestClient restClient = new RestClient(MESSAGE_SERVER_SOCKET);
    private final Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(MsClientRestAdapter.class);

    public void registerMsClient(MessageClientId messageClientId) {
        String json = gson.toJson(messageClientId);
        ResponseEntity<String> responseEntity = restClient.post(MsServerUriDictionary.API_SERVER_REGISTER, json);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            logger.error("Couldn't register message client {}", messageClientId);
            throw new RuntimeException(String.format("Failed to register message client %s in message server.", messageClientId));
        }
    }

    public boolean sendMessageToServer(Message message) {
        String json = gson.toJson(message);
        ResponseEntity<String> responseEntity = restClient.post(MsServerUriDictionary.API_SERVER_MESSAGE_SEND, json);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            logger.error("Code {} received! Message to server failed {}", responseEntity.getStatusCode(), message);
            return false;
        }
        return true;
    }
}
