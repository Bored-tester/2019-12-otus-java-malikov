package ru.otus.messagesystem.client;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.message.enums.MessageType;
import ru.otus.messagesystem.message.model.Message;
import ru.otus.messagesystem.message.model.MessageClientId;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MsClientImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    @Getter
    private final MessageClientId id;
    private final MsClientRestAdapter msClientRestAdapter;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();

    public MsClientImpl(String name, String port, MsClientRestAdapter msClientRestAdapter) {
        this.id = new MessageClientId(name, port);
        this.msClientRestAdapter = msClientRestAdapter;
        msClientRestAdapter.registerMsClient(id);
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public String getName() {
        return id.getName();
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = msClientRestAdapter.sendMessageToServer(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(id, new MessageClientId(to), null, msgType.getValue(), Serializers.serializeToJson(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(id, msClient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
