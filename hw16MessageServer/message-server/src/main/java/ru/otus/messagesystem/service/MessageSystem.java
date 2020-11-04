package ru.otus.messagesystem.service;


import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.message.model.MessageClientId;

public interface MessageSystem {

    void addClient(MessageClientId messageClientId);

    void removeClient(MessageClientId messageClientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}

