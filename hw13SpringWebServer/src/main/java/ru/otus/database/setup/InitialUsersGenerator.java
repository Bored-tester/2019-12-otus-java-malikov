package ru.otus.database.setup;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.database.core.enums.UserRole;
import ru.otus.database.core.model.AddressDataSet;
import ru.otus.database.core.model.PhoneDataSet;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DbUserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class InitialUsersGenerator implements InitialDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(InitialUsersGenerator.class);
    private final DbUserService dbUserService;

    @Override
    public void initialDataSetup(){
        logger.info("Going to init starting users");
        createAngel();
        createDemon();
        createOleg();
        logger.info("Starting users initialized");
    }

    private void createOleg(){
        AddressDataSet olegAddress = new AddressDataSet("Omsk");
        List<PhoneDataSet> olegPhones = Collections.singletonList(new PhoneDataSet("let-me-out"));
        User oleg = new User("Oleg", "Gaben", "1234", UserRole.MORTAL, olegAddress, olegPhones);

        dbUserService.saveUser(oleg);
    }

    private void createAngel(){
      AddressDataSet angelAddress = new AddressDataSet("Heaven");
        List<PhoneDataSet> angelsPhones = Collections.singletonList(new PhoneDataSet("777-777-777"));
        User angel = new User("Aziraphale", "angel3000", "qwerty", UserRole.ADMIN, angelAddress, angelsPhones);

        long angelId = dbUserService.saveUser(angel);
        dbUserService.getUser(angelId);
    }

    private void createDemon(){
        AddressDataSet demonAddress = new AddressDataSet("Hell");
        PhoneDataSet demonsPhone1 = new PhoneDataSet("666-666-666");
        PhoneDataSet demonsPhone2 = new PhoneDataSet("13-13-13");
        List<PhoneDataSet> demonsPhones = new ArrayList<>();
        demonsPhones.add(demonsPhone1);
        demonsPhones.add(demonsPhone2);
        User demon = new User("Crowley", "dImon", "666", UserRole.ADMIN, demonAddress, demonsPhones);

        long demonId = dbUserService.saveUser(demon);
        dbUserService.getUser(demonId);
    }
}
