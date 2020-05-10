package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;
import ru.otus.json.serializer.JsonSerializer;

import java.util.Map;

@AllArgsConstructor
public class JsonMapSerializer implements JsonElementSerializer {

    private Object mapObjectToSerialize;

    @Override
    public String convertValueToJsonFormat() {
        StringBuilder serializedMap = new StringBuilder("{");
        Map mapToSerialize = (Map) mapObjectToSerialize;
        mapToSerialize.forEach((key, value) -> {

//          serialize key
//          for some reason Gson casts all keys to String when serializing. I'll do the same
            serializedMap.append(String.format("\"%s\"", key.toString()));
            serializedMap.append(":");

//          serialize value
            serializedMap.append(JsonSerializer.toJson(value));

            serializedMap.append(",");
        });

//      delete last unnecessary comma
        serializedMap.deleteCharAt(serializedMap.length() - 1);
        serializedMap.append("}");
        return serializedMap.toString();
    }
}
