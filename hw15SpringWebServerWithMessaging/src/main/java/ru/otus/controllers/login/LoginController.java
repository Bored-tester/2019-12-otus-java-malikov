package ru.otus.controllers.login;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.app.common.Waiter;
import ru.otus.authentication.model.AuthUserData;
import ru.otus.controllers.login.handlers.AuthenticateUserByLoginResponseHandler;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.service.MessageSystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class LoginController {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 60;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final int USER_AUTHENTICATION_TIMEOUT_IN_SECONDS = 10;

    private final MsClient messageClient;
    @Getter
    private final Map<UUID, Boolean> authenticationResponsesMap = new ConcurrentHashMap<>();

    public LoginController(MessageSystem messageSystem) {
        messageClient = new MsClientImpl(ClientNameDictionary.LOGIN_CONTROLLER_CLIENT_NAME.getName(), messageSystem);
        messageClient.addHandler(MessageType.AUTHENTICATE_USER, new AuthenticateUserByLoginResponseHandler(this));
        messageSystem.addClient(messageClient);
    }

    @GetMapping("/login")
    protected String doGet() {
        return LOGIN_PAGE_TEMPLATE;
    }

    @PostMapping("/login")
    protected RedirectView doPost(HttpServletRequest request, HttpServletResponse response) {
        AuthUserData authUserData = new AuthUserData(request.getParameter(PARAM_LOGIN), request.getParameter(PARAM_PASSWORD));
        Message userAuthenticationMessage = messageClient.produceMessage(ClientNameDictionary.USER_AUTH_CLIENT_NAME.getName(), authUserData, MessageType.AUTHENTICATE_USER);
        messageClient.sendMessage(userAuthenticationMessage);

        Boolean authSuccessful = Waiter.waitForMessagePayloadInMap(userAuthenticationMessage.getId(), authenticationResponsesMap, USER_AUTHENTICATION_TIMEOUT_IN_SECONDS).orElse(false);

        if (authSuccessful) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            return new RedirectView("/users", true);
        } else {
            return new RedirectView("/login", true);
        }

    }

}
