package ru.otus.database;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.database.core.model.AddressDataSet;
import ru.otus.database.core.model.PhoneDataSet;
import ru.otus.database.core.model.User;
import ru.otus.database.hibernate.HibernateUtils;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.MsClientRestAdapter;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;

@Configuration
@ComponentScan("ru.otus.messagesystem.client.controller")
public class BackendConfig {
    String port = System.getProperty("server.port");

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);
    }

    @Bean
    public MsClient messageClient(MsClientRestAdapter msClientRestAdapter) {
        return new MsClientImpl(ClientNameDictionary.BACKEND_SERVICE_CLIENT_NAME.getName(), port, msClientRestAdapter);
    }
}
