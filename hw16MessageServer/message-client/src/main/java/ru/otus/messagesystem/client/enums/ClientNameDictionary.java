package ru.otus.messagesystem.client.enums;

import lombok.Getter;

public enum ClientNameDictionary {
    BACKEND_SERVICE_CLIENT_NAME("BackendService"),
    FRONTEND_SERVICE_CLIENT_NAME("FrontendService");

    @Getter
    private final String name;

    ClientNameDictionary(String name) {
        this.name = name;
    }
}
