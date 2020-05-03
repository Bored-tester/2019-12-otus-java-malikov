package ru.otus.json.serializer;

import ru.otus.json.serializer.handlers.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

class JsonElementSerializerMapper {
    static JsonElementSerializer getJsonElementSerializer(Object serializedObject) {
        if (serializedObject == null)
            return new JsonNullSerializer();

        Class classOfSerializedObject = serializedObject.getClass();

        if ((classOfSerializedObject.equals(String.class))
                || (classOfSerializedObject.equals(char.class))
                || (classOfSerializedObject.equals(Character.class)))
            return new JsonStringSerializer(serializedObject);

        if ((classOfSerializedObject.equals(byte.class))
                || (classOfSerializedObject.equals(short.class))
                || (classOfSerializedObject.equals(int.class))
                || (classOfSerializedObject.equals(long.class))
                || (classOfSerializedObject.equals(Byte.class))
                || (classOfSerializedObject.equals(Short.class))
                || (classOfSerializedObject.equals(Integer.class))
                || (classOfSerializedObject.equals(Long.class))
                || (classOfSerializedObject.equals(BigInteger.class))
                || (classOfSerializedObject.equals(float.class))
                || (classOfSerializedObject.equals(double.class))
                || (classOfSerializedObject.equals(BigDecimal.class))
                || (classOfSerializedObject.equals(Float.class))
                || (classOfSerializedObject.equals(Double.class))
        )
            return new JsonNumberSerializer(serializedObject);

        if ((classOfSerializedObject.equals(boolean.class))
                || (classOfSerializedObject.equals(Boolean.class))
        )
            return new JsonBooleanSerializer(serializedObject);

        if (classOfSerializedObject.isArray())
            return new JsonArraySerializer(serializedObject);

        if (Map.class.isAssignableFrom(classOfSerializedObject))
            return new JsonMapSerializer(serializedObject);

        if (Collection.class.isAssignableFrom(classOfSerializedObject))
            return new JsonCollectionSerializer(serializedObject);

        return new JsonCustomClassSerializer(serializedObject);
    }
}
