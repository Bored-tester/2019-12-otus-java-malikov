package ru.otus.atm;

import java.util.HashMap;
import java.util.Map;

class AtmAuth {
    private final Map<String, String> tokenLoginMap;
    private final Map<String, String> loginTokenMap;
    private long loginCounter;

    AtmAuth() {
        tokenLoginMap = new HashMap<>();
        loginTokenMap = new HashMap<>();
    }

    String generateToken(String login) {
        if (loginTokenMap.containsKey(login))
            deleteTokenForLogin(login);
        return createTokenFor(login);
    }

    private void deleteTokenForLogin(String login) {
        String token = loginTokenMap.get(login);
        loginTokenMap.remove(login);
        tokenLoginMap.remove(token);
    }

    public void deleteToken(String token) {
        String login = tokenLoginMap.get(token);
        if (login != null) {
            loginTokenMap.remove(login);
            tokenLoginMap.remove(token);
        }
    }

    private String createTokenFor(String login) {
        //insert proper security token generation lib here
        loginCounter++;
        String token = login + loginCounter;
        loginTokenMap.put(login, token);
        tokenLoginMap.put(token, login);
        return token;
    }

    String getLogin(String token) {
        return tokenLoginMap.get(token);
    }
}
