package ru.otus.messagesystem.client;


import ru.otus.messagesystem.message.model.Message;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
