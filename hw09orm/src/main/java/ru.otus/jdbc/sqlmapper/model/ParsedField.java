package ru.otus.jdbc.sqlmapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedField {
    private String fieldName;
    private Class fieldClass;
    private Object fieldValue;
}
