package ru.otus.messagesystem.message;

import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.message.model.Message;

public class ResponseMessageGenerator {
    public static <T> Message produceResponseMessage(Message request, T data) {
        return new Message(request.getTo(), request.getFrom(), request.getId(), request.getType(), Serializers.serializeToJson(data));
    }

    public static Message produceErrorResponseMessage(Message request, String errorMessage) {
        return new Message(request.getTo(), request.getFrom(), request.getId(), MessageType.ERROR.getValue(), Serializers.serializeToJson(errorMessage));
    }
}
