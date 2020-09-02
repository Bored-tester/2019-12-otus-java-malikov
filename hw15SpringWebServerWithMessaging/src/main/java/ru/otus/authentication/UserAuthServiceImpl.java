package ru.otus.authentication;


import org.springframework.stereotype.Service;
import ru.otus.authentication.handlers.AuthenticateUserByLoginRequestHandler;
import ru.otus.authentication.handlers.GetUserByLoginResponseHandler;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.service.MessageSystem;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final MsClient messageClient;
    private final Map<UUID, User> userResponseMap = new ConcurrentHashMap<>();

    public UserAuthServiceImpl(MessageSystem messageSystem) {
        messageClient = new MsClientImpl(ClientNameDictionary.USER_AUTH_CLIENT_NAME.getName(), messageSystem);
        messageClient.addHandler(MessageType.AUTHENTICATE_USER, new AuthenticateUserByLoginRequestHandler(this, messageClient, userResponseMap));
        messageClient.addHandler(MessageType.GET_USER_DATA_BY_LOGIN, new GetUserByLoginResponseHandler(this, userResponseMap));
        messageSystem.addClient(messageClient);
    }

}
