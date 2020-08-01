package ru.otus.controllers.users;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.app.common.Waiter;
import ru.otus.controllers.users.handlers.GetAllUsersReponseHandler;
import ru.otus.database.core.model.User;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.service.MessageSystem;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";
    private static final int GET_ALL_USERS_TIMEOUT_IN_SECONDS = 10;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final InputUserParser inputUserParser;
    private final MsClient messageClient;
    @Getter
    private final Map<UUID, List<User>> usersResponseMap = new ConcurrentHashMap<>();

    public UserController(InputUserParser inputUserParser, MessageSystem messageSystem) {
        this.inputUserParser = inputUserParser;
        messageClient = new MsClientImpl(ClientNameDictionary.USER_CONTROLLER_CLIENT_NAME.getName(), messageSystem);
        messageClient.addHandler(MessageType.GET_USERS_DATA, new GetAllUsersReponseHandler(this));
        messageSystem.addClient(messageClient);
    }

    @PostMapping("/api/user/create")
    public String userCreateView(HttpServletRequest request) throws IOException {
        String unparsedUser = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        User user = inputUserParser.parseIputUser(unparsedUser);
        Message createUserMessage = messageClient.produceMessage(ClientNameDictionary.DATABASE_SERVICE_CLIENT_NAME.getName(), user, MessageType.CREATE_USER);
        messageClient.sendMessage(createUserMessage);
        return USERS_PAGE_TEMPLATE;
    }

    @GetMapping("/users")
    protected String getAllUsers(Model model) {
        Message getAllUsersMessage = messageClient.produceMessage(ClientNameDictionary.DATABASE_SERVICE_CLIENT_NAME.getName(), null, MessageType.GET_USERS_DATA);
        messageClient.sendMessage(getAllUsersMessage);

        List<User> listOfAllUsers = Waiter.waitForMessagePayloadInMap(getAllUsersMessage.getId(), usersResponseMap, GET_ALL_USERS_TIMEOUT_IN_SECONDS).orElse(null);
        model.addAttribute(TEMPLATE_ATTR_USERS, listOfAllUsers);
        return USERS_PAGE_TEMPLATE;
    }
}
