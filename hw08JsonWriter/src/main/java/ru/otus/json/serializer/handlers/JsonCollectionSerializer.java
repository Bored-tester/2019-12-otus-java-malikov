package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;
import ru.otus.json.serializer.JsonSerializer;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JsonCollectionSerializer implements JsonElementSerializer {

    private Object collectionObjectToSerialize;

    @Override
    public String convertValueToJsonFormat() {
        Collection collectionToSerialize = (Collection) collectionObjectToSerialize;
        return (String) collectionToSerialize
                .stream()
                .map(JsonSerializer::toJson)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
