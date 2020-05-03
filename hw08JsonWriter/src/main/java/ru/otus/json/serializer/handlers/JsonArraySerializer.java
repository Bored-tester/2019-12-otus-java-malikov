package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;
import ru.otus.json.serializer.JsonSerializer;

import java.lang.reflect.Array;

public class JsonArraySerializer extends JsonElementSerializer {

    public JsonArraySerializer(Object objectToSerialize) {
        super(objectToSerialize);
    }

    @Override
    public String convertValueToJsonFormat() {
        StringBuilder serializedArray = new StringBuilder("[");
        for (int i = 0; i < Array.getLength(objectToSerialize); i++) {
            Object arrayElement = Array.get(objectToSerialize, i);
            String serializedElement = JsonSerializer.toJson(arrayElement);
            serializedArray.append(serializedElement);
            serializedArray.append(",");
        }
//      remove last comma
        serializedArray.deleteCharAt(serializedArray.length() - 1);
        serializedArray.append("]");
        return serializedArray.toString();
    }
}
