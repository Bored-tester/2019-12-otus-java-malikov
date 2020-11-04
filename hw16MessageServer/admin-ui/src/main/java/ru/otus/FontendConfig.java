package ru.otus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.MsClientRestAdapter;
import ru.otus.messagesystem.client.enums.ClientNameDictionary;

@Configuration
@ComponentScan("ru.otus.messagesystem.client.controller")
public class FontendConfig {
    String port = System.getProperty("server.port");

    @Bean
    public MsClient messageClient(MsClientRestAdapter msClientRestAdapter) {
        return new MsClientImpl(ClientNameDictionary.FRONTEND_SERVICE_CLIENT_NAME.getName(), port, msClientRestAdapter);
    }
}
