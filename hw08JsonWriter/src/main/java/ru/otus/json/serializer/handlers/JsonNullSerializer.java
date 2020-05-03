package ru.otus.json.serializer.handlers;

public class JsonNullSerializer implements JsonElementSerializer {

    @Override
    public String convertValueToJsonFormat() {
        return "null";
    }
}
