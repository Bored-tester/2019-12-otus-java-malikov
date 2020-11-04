package ru.otus.messagesystem.common;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializers {
    private static final Logger logger = LoggerFactory.getLogger(Serializers.class);

    private Serializers() {
    }

    public static byte[] serialize(Object data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(data);
            os.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("Serialization error, data:" + data, e);
            throw new RuntimeException("Serialization error:" + e.getMessage());
        }
    }

    public static byte[] serializeToJson(Object data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(baos)) {
            String json = new Gson().toJson(data);
            os.writeObject(json);
            os.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("Serialization error, data:" + data, e);
            throw new RuntimeException("Serialization error:" + e.getMessage());
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> classOfT) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream is = new ObjectInputStream(bis)) {
            Object obj = is.readObject();
            return classOfT.cast(obj);
        } catch (Exception e) {
            logger.error("DeSerialization error, classOfT:" + classOfT, e);
            throw new RuntimeException("DeSerialization error:" + e.getMessage());
        }
    }

    public static <T> T deserializeFromJson(byte[] data, Class<T> classOfT) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream is = new ObjectInputStream(bis)) {
            String json = (String) is.readObject();
            return new Gson().fromJson(json, classOfT);
        } catch (Exception e) {
            logger.error("DeSerialization error, classOfT:" + classOfT, e);
            throw new RuntimeException("DeSerialization error:" + e.getMessage());
        }
    }
}
