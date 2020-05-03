package ru.otus.json.serializer.handlers;

import ru.otus.json.serializer.JsonSerializer;

import java.util.Collection;
import java.util.stream.Collectors;

public class JsonCollectionSerializer extends JsonElementSerializer {

    public JsonCollectionSerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        Collection collectionToSerialize = (Collection) objectToSerialize;
        return (String) collectionToSerialize
                .stream()
                .map(JsonSerializer::toJson)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
