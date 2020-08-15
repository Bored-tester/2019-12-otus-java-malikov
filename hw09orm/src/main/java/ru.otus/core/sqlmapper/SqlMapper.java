package ru.otus.core.sqlmapper;

import java.sql.Connection;
import java.util.Optional;

public interface SqlMapper<T> {
    long insertRecord(Connection connection, T objectToInsert);

    Optional<T> selectRecord(Connection connection, Class<T> clazz, long id);
}
