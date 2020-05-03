package ru.otus.json.serializer.handlers;

import ru.otus.json.serializer.JsonSerializer;

import java.util.Map;

public class JsonMapSerializer extends JsonElementSerializer {

    public JsonMapSerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        StringBuilder serializedMap = new StringBuilder("{");
        Map arrayToSerialize = (Map) objectToSerialize;
        arrayToSerialize.forEach((key, value) -> {

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
