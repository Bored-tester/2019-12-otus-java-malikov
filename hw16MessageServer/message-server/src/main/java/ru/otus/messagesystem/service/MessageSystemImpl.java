package ru.otus.messagesystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.message.model.MessageClientId;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public final class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final MessageClientStorage messageClientStorage;
    private final MsServerRestAdapter msServerRestAdapter;
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private Runnable disposeCallback;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {

                private final AtomicInteger threadNameSeq = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
                    return thread;
                }
            });

    public MessageSystemImpl(MessageClientStorage messageClientStorage, MsServerRestAdapter msServerRestAdapter) {
        this.messageClientStorage = messageClientStorage;
        this.msServerRestAdapter = msServerRestAdapter;
        start();
    }

    @Override
    public void start() {
        msgProcessor.submit(this::processMessages);
    }

    @Override
    public int currentQueueSize() {
        return messageQueue.size();
    }

    @Override
    public void addClient(MessageClientId messageClientId) {
        logger.info("new client:{}", messageClientId);
        messageClientStorage.addClient(messageClientId);
        logger.info(String.format("New client %s was registered on port: %s", messageClientId.getName(), messageClientId.getPort()));
    }

    @Override
    public void removeClient(MessageClientId messageClientId) {
        messageClientStorage.removeClient(messageClientId);
    }

    @Override
    public boolean newMessage(Message msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            logger.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        logger.info("now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    private void processMessages() {
        logger.info("msgProcessor started, {}", currentQueueSize());
        while (runFlag.get() || !messageQueue.isEmpty()) {
            try {
                Message msg = messageQueue.take();
                if (msg == Message.VOID_MESSAGE) {
                    logger.info("received the stop message");
                } else {
                    MessageClientId destinationClientId = msg.getTo();
                    if (destinationClientId.getPort() == null) {
                        Optional<MessageClientId> fullDestinationClientId = messageClientStorage.getClientIdByName(destinationClientId.getName());
                        if (fullDestinationClientId.isEmpty()) {
                            logger.error("Failed to send message {}\nNo receiver found!", msg);
                            continue;
                        }
                        msg.setTo(fullDestinationClientId.get());
                    }
                    msgHandler.submit(() -> handleMessage(msg));
                }
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        if (disposeCallback != null) {
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
        logger.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        logger.info("msgHandler has been shut down");
    }


    private void handleMessage(Message msg) {
        try {
            msServerRestAdapter.sendMessageToClient(msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("message:{}", msg);
        }
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.VOID_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.VOID_MESSAGE);
        }
    }

}
