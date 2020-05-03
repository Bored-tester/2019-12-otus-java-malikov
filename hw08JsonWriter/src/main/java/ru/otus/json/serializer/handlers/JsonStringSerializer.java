package ru.otus.json.serializer.handlers;

public class JsonStringSerializer extends JsonElementSerializer {
    public JsonStringSerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        return String.format("\"%s\"", objectToSerialize);
    }
}
