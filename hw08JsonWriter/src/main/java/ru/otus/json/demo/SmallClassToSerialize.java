package ru.otus.json.demo;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class SmallClassToSerialize {
    String littleName;
    int[] littleArray;

    @Override
    public String toString() {
        return littleName + " "
                + Arrays.toString(littleArray);
    }
}
