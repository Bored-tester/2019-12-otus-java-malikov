package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonBooleanSerializer implements JsonElementSerializer {
    private Object booleanToSerialize;

    @Override
    public String convertValueToJsonFormat() {
        return String.format("%s", booleanToSerialize);
    }
}
