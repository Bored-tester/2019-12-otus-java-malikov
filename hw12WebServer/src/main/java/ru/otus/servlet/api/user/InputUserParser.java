package ru.otus.servlet.api.user;

import com.google.gson.Gson;
import ru.otus.database.core.enums.UserRole;
import ru.otus.database.core.model.AddressDataSet;
import ru.otus.database.core.model.PhoneDataSet;
import ru.otus.database.core.model.User;
import ru.otus.servlet.api.user.model.InputUser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class InputUserParser {
    static User parseIputUser(String inputUserJson) throws IllegalArgumentException {
        InputUser unparsedUser = new Gson().fromJson(inputUserJson, InputUser.class);
        List<PhoneDataSet> phones = Arrays.stream(unparsedUser.getPhones())
                .map(PhoneDataSet::new)
                .collect(Collectors.toList());
        return new User(unparsedUser.getName(),
                unparsedUser.getLogin(),
                unparsedUser.getPassword(),
                UserRole.valueOf(unparsedUser.getRole()),
                new AddressDataSet(unparsedUser.getAddress()),
                phones);
    }
}
