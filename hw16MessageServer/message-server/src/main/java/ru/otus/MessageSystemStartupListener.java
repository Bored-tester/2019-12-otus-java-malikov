package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class MessageSystemStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemStartupListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Message server started. Starting 2 frontends and 2 backends");

        ProcessBuilder adminBackend1 = new ProcessBuilder("java", "-Dserver.port=8084", "-jar", "./admin-backend.jar");
        adminBackend1.directory(new File("../../admin-backend/target"));
        Map<String, String> envVariables = adminBackend1.environment();
        adminBackend1.redirectOutput(new File("./admin-backend1.log"));
        adminBackend1.redirectErrorStream(true);
        try {
            long pid = adminBackend1.start().pid();
            logger.info("Admin backend 1 started with pid {}", pid);
        } catch (IOException e) {
            logger.info("Failed to start first admin backend app: {}\n{}", e.toString(), e.getStackTrace());
            throw new RuntimeException(e);
        }

        ProcessBuilder adminBackend2 = new ProcessBuilder("java", "-Dserver.port=8085", "-jar", "./admin-backend.jar");
        adminBackend2.directory(new File("../../admin-backend/target"));
        adminBackend2.redirectOutput(new File("./admin-backend2.log"));
        adminBackend2.redirectErrorStream(true);
        try {
            long pid = adminBackend2.start().pid();
            logger.info("Admin backend 2 started with pid {}", pid);
        } catch (IOException e) {
            logger.info("Failed to start second admin backend app: {}\n{}", e.toString(), e.getStackTrace());
            throw new RuntimeException(e);
        }

        ProcessBuilder adminUI1 = new ProcessBuilder("java", "-Dserver.port=8086", "-jar", "./admin-ui.jar");
        adminUI1.directory(new File("../../admin-ui/target"));
        adminUI1.redirectOutput(new File("./admin-ui1.log"));
        adminUI1.redirectErrorStream(true);
        try {
            long pid = adminUI1.start().pid();
            logger.info("Admin UI 1 started with pid {}", pid);
        } catch (IOException e) {
            logger.info("Failed to start first admin ui app: {}\n{}", e.toString(), e.getStackTrace());
            throw new RuntimeException(e);
        }

        ProcessBuilder adminUI2 = new ProcessBuilder("java", "-Dserver.port=8087", "-jar", "./admin-ui.jar");
        adminUI2.directory(new File("../../admin-ui/target"));
        adminUI2.redirectOutput(new File("./admin-ui2.log"));
        adminUI2.redirectErrorStream(true);
        try {
            long pid = adminUI2.start().pid();
            logger.info("Admin UI 2 started with pid {}", pid);
        } catch (IOException e) {
            logger.info("Failed to start second admin ui app: {}\n{}", e.toString(), e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
