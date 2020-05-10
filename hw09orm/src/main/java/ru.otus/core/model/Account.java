package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.core.annotations.Id;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class Account {
    @Id
    private final BigInteger no;
    private final String type;
    private final int rest;

    @Override
    public String toString() {
        return "User{" +
                "no=" + no +
                ", type='" + type +
                ", rest='" + rest + '\'' +
                '}';
    }
}
