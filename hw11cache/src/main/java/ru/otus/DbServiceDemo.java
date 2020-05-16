package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUserImpl dbServiceUser = new DbServiceUserImpl(userDao);

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
        for (int i = 258; i > 0; i--) {
            AddressDataSet angelCloneAddress = new AddressDataSet("Heaven");
            List<PhoneDataSet> angelsClonePhones = Collections.singletonList(new PhoneDataSet("777-777-777"));
            User angelClone = new User("AziraphaleClone", angelCloneAddress, angelsClonePhones);
            dbServiceUser.saveUser(angelClone);
        }

        long demonId = dbServiceUser.saveUser(demon);
        Optional<User> kindOfDemon = dbServiceUser.getUser(demonId);

        long angelId = dbServiceUser.saveUser(angel);
        Optional<User> kindOfAngel = dbServiceUser.getUser(angelId);
        logger.info("before gc: {}", dbServiceUser.getUserCache().getSize());

        outputUserOptional("Demon user", kindOfDemon);
        outputUserOptional("Angel user", kindOfAngel);
        try {
            System.gc();
            Thread.sleep(100);
            logger.info("after gc: {}", dbServiceUser.getUserCache().getSize());
        } catch (InterruptedException e) {
        }

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
