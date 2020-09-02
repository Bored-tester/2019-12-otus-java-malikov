package ru.otus.messagesystem.message;

import ru.otus.app.common.Serializers;
import ru.otus.messagesystem.message.enums.MessageType;

public class ResponseMessageGenerator {
    public static <T> Message produceResponseMessage(Message request, T data) {
        return new Message(request.getTo(), request.getFrom(), request.getId(), request.getType(), Serializers.serialize(data));
    }

    public static Message produceErrorResponseMessage(Message request, String errorMessage) {
        return new Message(request.getTo(), request.getFrom(), request.getId(), MessageType.ERROR.getValue(), Serializers.serialize(errorMessage));
    }
}
