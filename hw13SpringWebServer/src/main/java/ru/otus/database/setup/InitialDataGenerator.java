package ru.otus.database.setup;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.database.core.enums.UserRole;
import ru.otus.database.core.model.AddressDataSet;
import ru.otus.database.core.model.PhoneDataSet;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbUserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface InitialDataGenerator {
    void initialDataSetup();
}
