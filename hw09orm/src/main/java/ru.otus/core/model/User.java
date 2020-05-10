package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.core.annotations.Id;

@Getter
@AllArgsConstructor
public class User {
    @Id
    private final long id;
    private final String name;
    private final int age;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name +
                ", age='" + age + '\'' +
                '}';
    }
}
