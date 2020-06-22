package ru.otus.controllers.user.model;

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
