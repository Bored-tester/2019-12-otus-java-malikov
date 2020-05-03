package ru.otus.json.serializer.handlers;

import lombok.AllArgsConstructor;
import ru.otus.json.serializer.JsonSerializer;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JsonCustomClassSerializer extends JsonElementSerializer {
    private Field[] classFields;

    public JsonCustomClassSerializer(Object objectToSerialize) {
        super(objectToSerialize);
        this.classFields = objectToSerialize.getClass().getDeclaredFields();
    }

    @Override
    public String convertValueToJsonFormat() {
        StringBuilder serializedObject = new StringBuilder("{");
        Arrays.stream(classFields)
                .forEach(field -> {
                    try {
//                  add field name
                        serializedObject.append(String.format("\"%s\"", field.getName()));
                        serializedObject.append(":");

//                  add field value
                        field.setAccessible(true);
                        Object fieldValue = field.get(objectToSerialize);

                        serializedObject.append(JsonSerializer.toJson(fieldValue));
                    } catch (IllegalAccessException e) {
                        System.out.println(String.format("Error! Trying to get field %s from object of class %s", field.getName(), objectToSerialize.getClass().getName()));
                    }
                    serializedObject.append(",");

                });

//      delete last unnecessary comma
        serializedObject.deleteCharAt(serializedObject.length() - 1);
        serializedObject.append("}");
        return serializedObject.toString();
    }
}
