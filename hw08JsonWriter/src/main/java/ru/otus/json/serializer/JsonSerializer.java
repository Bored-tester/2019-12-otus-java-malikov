package ru.otus.json.serializer;

public class JsonSerializer {
    public static String toJson(Object serializedObject) {
        return JsonElementSerializerMapper.getJsonElementSerializer(serializedObject).convertValueToJsonFormat();
    }
}
