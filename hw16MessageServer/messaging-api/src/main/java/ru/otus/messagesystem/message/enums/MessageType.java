package ru.otus.messagesystem.message.enums;

public enum MessageType {
    GET_USER_DATA_BY_LOGIN("GetUserDataByLogin"),
    GET_USER_DATA_BY_ID("GetUserDataById"),
    GET_USERS_DATA("GetUsersData"),
    CREATE_USER("CreateUser"),
    ERROR("Error");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
