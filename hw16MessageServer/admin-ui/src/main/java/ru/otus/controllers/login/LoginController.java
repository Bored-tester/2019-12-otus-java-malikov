package ru.otus.controllers.login;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.common.Waiter;
import ru.otus.common.enums.UserRole;
import ru.otus.common.model.User;
import ru.otus.controllers.login.handlers.GetUserByLoginResponseHandler;
import ru.otus.controllers.login.model.AuthUserData;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.message.model.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LoginController {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 300;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final int USER_AUTHENTICATION_TIMEOUT_IN_SECONDS = 10;

    private final MsClient messageClient;
    @Getter
    private final Map<UUID, User> userResponsesMap = new ConcurrentHashMap<>();
    ;

    public LoginController(MsClient messageClient) {
        messageClient.addHandler(MessageType.GET_USER_DATA_BY_LOGIN, new GetUserByLoginResponseHandler(userResponsesMap));
        this.messageClient = messageClient;
    }

    @GetMapping("/login")
    protected String doGet() {
        return LOGIN_PAGE_TEMPLATE;
    }

    @PostMapping("/login")
    protected RedirectView doPost(HttpServletRequest request, HttpServletResponse response) {
        AuthUserData authUserData = new AuthUserData(request.getParameter(PARAM_LOGIN), request.getParameter(PARAM_PASSWORD));

        Message getUserByLoginMessage = messageClient.produceMessage(ClientNameDictionary.BACKEND_SERVICE_CLIENT_NAME.getName(), authUserData.getLogin(), MessageType.GET_USER_DATA_BY_LOGIN);
        messageClient.sendMessage(getUserByLoginMessage);

        Optional<User> user = Waiter.waitForMessagePayloadInMap(getUserByLoginMessage.getId(), userResponsesMap, USER_AUTHENTICATION_TIMEOUT_IN_SECONDS);
        Boolean isAuthenticated = user
                .map(loggingInUser -> (loggingInUser.getPassword().equals(authUserData.getPassword())) && (loggingInUser.getRole().equals(UserRole.ADMIN)))
                .orElse(false);

        if (isAuthenticated) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            return new RedirectView("/users", true);
        } else {
            return new RedirectView("/login", true);
        }

    }

}
