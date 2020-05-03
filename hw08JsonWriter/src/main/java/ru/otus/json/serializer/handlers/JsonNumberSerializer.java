package ru.otus.json.serializer.handlers;

public class JsonNumberSerializer extends JsonElementSerializer {
    public JsonNumberSerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        return objectToSerialize.toString();
    }
}
