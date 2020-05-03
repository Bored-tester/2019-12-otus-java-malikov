package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class JsonElementSerializer {
    protected Object objectToSerialize;

    public abstract String convertValueToJsonFormat();
}
