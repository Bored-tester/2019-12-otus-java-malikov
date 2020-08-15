package ru.otus;

import org.hibernate.SessionFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbServiceDemo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        AddressDataSet demonAddress = new AddressDataSet("Hell");
        PhoneDataSet demonsPhone1 = new PhoneDataSet("666-666-666");
        PhoneDataSet demonsPhone2 = new PhoneDataSet("13-13-13");
        List<PhoneDataSet> demonsPhones = new ArrayList<>();
        demonsPhones.add(demonsPhone1);
        demonsPhones.add(demonsPhone2);
        User demon = new User("Crowley", demonAddress, demonsPhones);

        AddressDataSet angelAddress = new AddressDataSet("Heaven");
        List<PhoneDataSet> angelsPhones = Collections.singletonList(new PhoneDataSet("777-777-777"));
        User angel = new User("Aziraphale", angelAddress, angelsPhones);

        long demonId = dbServiceUser.saveUser(demon);
        Optional<User> kindOfDemon = dbServiceUser.getUser(demonId);

        long angelId = dbServiceUser.saveUser(angel);
        Optional<User> kindOfAngel = dbServiceUser.getUser(angelId);

        outputUserOptional("Demon user", kindOfDemon);
        outputUserOptional("Angel user", kindOfAngel);
    }

    private static void outputUserOptional(String header, Optional<User> mayBeUser) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(header);
        User user = mayBeUser.orElseThrow(
                () -> new RuntimeException("User not found!")
        );
        System.out.println(user);
    }
}
