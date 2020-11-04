package ru.otus.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.common.enums.UserRole;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class User implements Serializable {

    private String name;
    private String login;
    private String password;
    private UserRole role;
    private AddressDataSet address;
    private List<PhoneDataSet> phones;

    public User() {
    }

    @Override
    public String toString() {
        return String.format("User:\nname %s\naddress %s\nphones %s",
//                id,
                name,
                address,
                phones.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));
    }
}
