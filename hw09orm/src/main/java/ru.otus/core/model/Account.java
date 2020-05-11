package ru.otus.core.model;

import lombok.Getter;
import ru.otus.core.annotations.Id;

import java.math.BigInteger;

@Getter
public class Account {
    @Id
    private BigInteger no;
    private final String type;
    private final int rest;

    public Account(String type, int rest) {
        this.type = type;
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "User{" +
                "no=" + no +
                ", type='" + type +
                ", rest='" + rest + '\'' +
                '}';
    }
}
