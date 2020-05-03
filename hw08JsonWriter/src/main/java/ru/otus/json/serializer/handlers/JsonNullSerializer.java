package ru.otus.json.serializer.handlers;

public class JsonNullSerializer extends JsonElementSerializer {
    public JsonNullSerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        return "null";
    }
}
