package ru.otus.json.serializer.handlers;

public class JsonBooleanSerializer extends JsonElementSerializer {

    public JsonBooleanSerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        return String.format("%s", objectToSerialize);
    }
}
