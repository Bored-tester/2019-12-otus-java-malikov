package ru.otus.controllers.users;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.controllers.users.handlers.GetAllUsersReponseHandler;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.service.MessageSystem;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class UserApiController {

    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private final MsClient messageClient;

    @Getter
    private final Map<UUID, List<User>> usersResponseMap = new ConcurrentHashMap<>();

    public UserApiController(MessageSystem messageSystem, GetAllUsersReponseHandler getAllUsersReponseHandler) {
        messageClient = new MsClientImpl(ClientNameDictionary.USER_CONTROLLER_CLIENT_NAME.getName(), messageSystem);
        messageClient.addHandler(MessageType.GET_USERS_DATA, getAllUsersReponseHandler);
        messageSystem.addClient(messageClient);
    }

    @PostMapping("/api/user/create")
    public String createUser(@RequestBody User user) {
        Message createUserMessage = messageClient.produceMessage(ClientNameDictionary.DATABASE_SERVICE_CLIENT_NAME.getName(), user, MessageType.CREATE_USER);
        messageClient.sendMessage(createUserMessage);
        return "User creation request was successfully sent.";
    }

    @GetMapping("/api/users")
    protected String getAllUsers(HttpServletRequest request) {
        Message getAllUsersMessage = messageClient.produceMessage(ClientNameDictionary.DATABASE_SERVICE_CLIENT_NAME.getName(), null, MessageType.GET_USERS_DATA);
        messageClient.sendMessage(getAllUsersMessage);
        return "Get users request was sent succesfully.";
    }
}
