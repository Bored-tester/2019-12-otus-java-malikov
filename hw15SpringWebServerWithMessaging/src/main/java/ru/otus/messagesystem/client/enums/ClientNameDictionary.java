package ru.otus.messagesystem.client.enums;

import lombok.Getter;

public enum ClientNameDictionary {
    DATABASE_SERVICE_CLIENT_NAME("DatabaseService"),
    USER_CONTROLLER_CLIENT_NAME("UserController"),
    USER_AUTH_CLIENT_NAME("UserAuthentication"),
    LOGIN_CONTROLLER_CLIENT_NAME("LoginController");

    @Getter
    private final String name;

    ClientNameDictionary(String name) {
        this.name = name;
    }
}
