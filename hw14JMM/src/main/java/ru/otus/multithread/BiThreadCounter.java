package ru.otus.multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BiThreadCounter {
    private static final Logger logger = LoggerFactory.getLogger(BiThreadCounter.class);
    private static final int THREAD_COUNT = 2;
    private final Semaphore semaphore = new Semaphore(THREAD_COUNT - 1);
    private final List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        BiThreadCounter biThreadCounter = new BiThreadCounter();
        biThreadCounter.go();
    }

    public void go() throws InterruptedException {
        logger.info("starting");
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.add(new CountingThread(semaphore));
        }

        threads.parallelStream().forEach(Thread::start);

        for (Thread thread : threads) {
            thread.join();
        }

        logger.info("finished");
    }

}
