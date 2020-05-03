package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonNumberSerializer implements JsonElementSerializer {
private Object numberToSerialize;

    @Override
    public String convertValueToJsonFormat() {
        return numberToSerialize.toString();
    }
}
