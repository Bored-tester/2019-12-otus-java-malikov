package ru.otus.jdbc.sqlmapper;

import ru.otus.core.annotations.Id;
import ru.otus.jdbc.sqlmapper.model.MetaObject;
import ru.otus.jdbc.sqlmapper.model.ParsedField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetaObjectParser {
    public static MetaObject parseObjectForSqlGeneration(Object objectToParse) {
        MetaObject.MetaObjectBuilder parsedObjectBuilder = parseClassPart(objectToParse.getClass());

        List<Field> fields = Arrays.asList(objectToParse.getClass().getDeclaredFields());
        if (fields.size() == 0) {
            throw new IllegalArgumentException("Object passed for parsing has no fields!");
        }

        List<ParsedField> parsedFields = new ArrayList<>();
        ParsedField parsedIdField = null;
        for (Field field : fields) {
            ParsedField parsedField = new ParsedField(field.getName(), field.getType(), getFieldValue(field, objectToParse));
            if (field.isAnnotationPresent(Id.class)) {
                if (parsedIdField != null)
                    throw new IllegalArgumentException("Object passed for parsing has more than one id field!");
                parsedIdField = parsedField;
            } else
                parsedFields.add(parsedField);
        }

        return parsedObjectBuilder
                .id(parsedIdField)
                .nonIdFields(parsedFields)
                .build();
    }

    public static MetaObject parseClassForSqlGeneration(Class classToParse) {
        MetaObject.MetaObjectBuilder parsedObjectBuilder = parseClassPart(classToParse);

        List<Field> fields = Arrays.asList(classToParse.getDeclaredFields());
        if (fields.size() == 0) {
            throw new IllegalArgumentException("Object passed for parsing has no fields!");
        }

        List<ParsedField> parsedFields = new ArrayList<>();
        ParsedField parsedIdField = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (parsedIdField != null)
                    throw new IllegalArgumentException("Object passed for parsing has more than one id field!");
                parsedIdField = new ParsedField(field.getName(), field.getType(), null);
            } else
                parsedFields.add(new ParsedField(field.getName(), field.getType(), null));
        }

        return parsedObjectBuilder
                .id(parsedIdField)
                .nonIdFields(parsedFields)
                .build();
    }

    private static MetaObject.MetaObjectBuilder parseClassPart(Class classToParse) {
        MetaObject.MetaObjectBuilder parsedObjectBuilder = MetaObject.builder();
        parsedObjectBuilder.tableName(classToParse.getSimpleName());
        parsedObjectBuilder.clazz(classToParse);
        return parsedObjectBuilder;
    }

    private static Object getFieldValue(Field field, Object fieldHolder) {
        try {
            field.setAccessible(true);
            return field.get(fieldHolder);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(String.format("Error! Can not get field %s from object of class %s", field.getName(), fieldHolder.getClass().getName()));
        }
    }
}
