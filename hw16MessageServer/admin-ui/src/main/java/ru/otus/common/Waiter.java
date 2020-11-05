package ru.otus.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Waiter {
    private static final Logger logger = LoggerFactory.getLogger(Waiter.class);

    public static <T> Optional<T> waitForMessagePayloadInMap(UUID messageId, Map<UUID, T> messageMap, int timeoutInSeconds) {
        while (timeoutInSeconds > 0) {
            if (messageMap.containsKey(messageId)) {
                return Optional.of(messageMap.remove(messageId));
            }
            logger.info(String.format("Response to request %s is not received. Waiting for another %d seconds", messageId, timeoutInSeconds));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                logger.info("Caught interrupted exception while waiting for the list of all users.");
            }
            timeoutInSeconds--;
        }
        logger.error(String.format("Request %s timed out after %d seconds.", messageId, timeoutInSeconds));
        return Optional.empty();
    }
}
