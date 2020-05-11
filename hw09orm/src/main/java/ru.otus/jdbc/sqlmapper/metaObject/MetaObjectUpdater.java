package ru.otus.jdbc.sqlmapper.metaObject;

import ru.otus.core.sqlmapper.SqlMapperException;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Optional;

public class MetaObjectUpdater {
    public static void setId(long id, Field idField, Object objectToUpdate) {
        Object castId = castId(id, idField.getType()).orElseThrow(
                () -> new SqlMapperException(new IllegalArgumentException("Object was created but id field has incorrect class. Should be long or BigInteger."))
        );
        try {
            idField.setAccessible(true);
            idField.set(objectToUpdate, castId);
        } catch (IllegalAccessException e) {
            System.out.println("Error! Id field is not accessible.");

        }
    }

    private static Optional<Object> castId(long id, Class idFieldClass) {

        if (idFieldClass.equals(long.class) || idFieldClass.equals(Long.class))
            return Optional.of(id);
        if (idFieldClass.equals(BigInteger.class))
            return Optional.of(BigInteger.valueOf(id));

        System.out.println(String.format("Error! Id field class is %s but should be long or BigInteger.", idFieldClass.getName()));
        return Optional.empty();
    }
}
