package ru.otus.database;

import org.hibernate.SessionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.otus.database.core.model.AddressDataSet;
import ru.otus.database.core.model.PhoneDataSet;
import ru.otus.database.core.model.User;
import ru.otus.database.hibernate.HibernateUtils;
import ru.otus.database.setup.InitialDataGenerator;

@Configuration
public class DbConfig {
    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);
    }

    @Bean
    CommandLineRunner initDatabase(InitialDataGenerator initialDataGenerator) {
        return args -> initialDataGenerator.initialDataSetup();
    }
}
