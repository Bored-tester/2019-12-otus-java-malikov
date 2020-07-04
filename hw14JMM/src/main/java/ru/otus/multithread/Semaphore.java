package ru.otus.multithread;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Semaphore {
    @Getter
    private int value = 0;
    private final int maxValue;

    public int next() {
        if (value == maxValue)
            value = 0;
        else
            value++;
        return value;
    }
}
