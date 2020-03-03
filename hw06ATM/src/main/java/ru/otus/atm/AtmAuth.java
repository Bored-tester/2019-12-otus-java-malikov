package ru.otus.atm;

interface AtmAuth {

    String generateToken(String login);

    void deleteToken(String token);

    String getLogin(String token);
}
