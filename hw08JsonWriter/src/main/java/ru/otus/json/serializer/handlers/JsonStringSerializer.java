package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonStringSerializer implements JsonElementSerializer {
private Object stringToSerialize;

    @Override
    public String convertValueToJsonFormat() {
        return String.format("\"%s\"", stringToSerialize);
    }
}
