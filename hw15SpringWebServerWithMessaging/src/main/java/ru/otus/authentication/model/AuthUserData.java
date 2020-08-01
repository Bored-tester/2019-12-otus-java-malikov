package ru.otus.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthUserData implements Serializable {
    private String login;
    private String password;
}
