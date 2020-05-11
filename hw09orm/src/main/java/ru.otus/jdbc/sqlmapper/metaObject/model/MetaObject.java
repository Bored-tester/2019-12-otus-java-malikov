package ru.otus.jdbc.sqlmapper.metaObject.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MetaObject {
    private String tableName;
    private Class clazz;
    private ParsedField id;
    private List<ParsedField> nonIdFields;

    public String getNonIdFieldNames() {
        List<String> nonIdFieldNames = this.getNonIdFields().stream()
                .map(ParsedField::getFieldName)
                .collect(Collectors.toList());
        return String.join(", ", nonIdFieldNames);
    }

    public List<String> getNonIdFieldValues() {
        return this.getNonIdFields().stream()
                .map(ParsedField::getFieldValue)
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
