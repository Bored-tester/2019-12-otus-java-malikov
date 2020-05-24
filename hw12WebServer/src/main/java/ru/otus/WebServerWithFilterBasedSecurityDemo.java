package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
import ru.otus.database.core.dao.UserDao;
import ru.otus.database.core.enums.UserRole;
import ru.otus.database.core.model.AddressDataSet;
import ru.otus.database.core.model.PhoneDataSet;
import ru.otus.database.core.model.User;
import ru.otus.database.core.service.DBServiceUser;
import ru.otus.database.hibernate.HibernateUtils;
import ru.otus.database.hibernate.dao.UserDaoHibernate;
import ru.otus.database.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
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
        User demon = new User("Crowley", "dImon", "666", UserRole.ADMIN, demonAddress, demonsPhones);

        AddressDataSet angelAddress = new AddressDataSet("Heaven");
        List<PhoneDataSet> angelsPhones = Collections.singletonList(new PhoneDataSet("777-777-777"));
        User angel = new User("Aziraphale", "angel3000", "qwerty", UserRole.ADMIN, angelAddress, angelsPhones);

        AddressDataSet olegAddress = new AddressDataSet("Omsk");
        List<PhoneDataSet> olegPhones = Collections.singletonList(new PhoneDataSet("let-me-out"));
        User oleg = new User("Oleg", "Gaben", "1234", UserRole.MORTAL, olegAddress, olegPhones);

        long demonId = dbServiceUser.saveUser(demon);
        dbServiceUser.getUser(demonId);

        long angelId = dbServiceUser.saveUser(angel);
        dbServiceUser.getUser(angelId);

        dbServiceUser.saveUser(oleg);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
