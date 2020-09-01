package ru.otus.appcontainer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppComponentItem {
    private final String name;
    private final Object itemValue;
}
