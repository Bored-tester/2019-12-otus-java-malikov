package ru.otus.controllers.users.model;

import lombok.Getter;

@Getter
public class InputUser {
    private String name;
    private String login;
    private String password;
    private String role;
    private String address;
    private String[] phones;
}
