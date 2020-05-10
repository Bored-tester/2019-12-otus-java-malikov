package ru.otus.json.demo;

import com.google.gson.Gson;
import ru.otus.json.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class JsonDemo {
    public static void main(String[] args) {
        BigClassToSerialize classToSerialize = new BigClassToSerialize();

        SmallClassToSerialize smallClassToSerialize1 = new SmallClassToSerialize("Small name", new int[]{1, 2});
        SmallClassToSerialize smallClassToSerialize2 = new SmallClassToSerialize("Very small name", new int[]{10, 20});

        Set<SmallClassToSerialize> smallSet = new HashSet<>();
        smallSet.add(smallClassToSerialize1);
        smallSet.add(smallClassToSerialize2);

        Map<Integer, SmallClassToSerialize> smallMap = new HashMap<>();
        smallMap.put(1, smallClassToSerialize1);
        smallMap.put(2, smallClassToSerialize2);

        Map<SmallClassToSerialize, Integer> smallReversedMap = new HashMap<>();
        smallReversedMap.put(smallClassToSerialize1, 1);
        smallReversedMap.put(smallClassToSerialize2, 2);

        classToSerialize.setBigMap(smallMap);
        classToSerialize.setBigSet(smallSet);

        System.out.println("We are going to serialize this object:\n" + classToSerialize + "\n");

//        String json = new Gson().toJson(classToSerialize);
        String json = JsonSerializer.toJson(classToSerialize);
        System.out.println("Serialized object:\n" + json + "\n");

        BigClassToSerialize deserializedClass = new Gson().fromJson(json, BigClassToSerialize.class);
        System.out.println("Compare original class to a deserialized one. Is similar: " + classToSerialize.toString().equals(deserializedClass.toString()));
        System.out.println("Deserialized object:\n" + deserializedClass);
    }
}
