package ru.otus.core.model;

import lombok.Getter;
import ru.otus.core.annotations.Id;

@Getter
public class User {
    @Id
    private long id;
    private final String name;
    private final int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name +
                ", age='" + age + '\'' +
                '}';
    }
}
