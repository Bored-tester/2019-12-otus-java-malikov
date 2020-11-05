package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class UserAdminMessageServer {
    public static final String SERVER_PORT = "8083";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserAdminMessageServer.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", SERVER_PORT));
        app.run(args);
    }

}
