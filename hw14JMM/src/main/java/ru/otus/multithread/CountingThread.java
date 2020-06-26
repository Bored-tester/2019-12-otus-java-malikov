package ru.otus.multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CountingThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(CountingThread.class);
    private static final int MAX_COUNT = 10;
    private final Semaphore semaphore;
    private final int threadId;

    public CountingThread(Semaphore semaphore) {
        super();
        this.semaphore = semaphore;
        threadId = semaphore.next();
    }

    @Override
    public void run() {
        logger.info("Thread started with id: " + threadId);
        for (int i = 1; i <= MAX_COUNT; i++) {
            while (semaphore.getValue() != threadId) {
                try {
                    Thread.sleep(1);
//                    logger.info("Waiting for semaphore. Current value = " + semaphore.getValue());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            logger.info("" + i);
            semaphore.next();
            sleep();
        }

        for (int i = MAX_COUNT - 1; i > 0; i--) {
            while (semaphore.getValue() != threadId) {

                try {
                    Thread.sleep(1);
//                    logger.info("Waiting for semaphore. Current value = " + semaphore.getValue());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            logger.info("" + i);
            semaphore.next();

            sleep();
        }
        logger.info("Thread finished");
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(new Random().nextInt(2)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
