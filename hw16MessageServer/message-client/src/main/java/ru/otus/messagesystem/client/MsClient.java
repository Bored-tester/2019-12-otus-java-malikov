package ru.otus.messagesystem.client;


import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.message.model.Message;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);

}
