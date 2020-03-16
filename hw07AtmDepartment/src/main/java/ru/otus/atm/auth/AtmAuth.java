package ru.otus.atm.auth;

public interface AtmAuth {

    String generateToken(String login);

    void deleteToken(String token);

    String getLogin(String token);
}
