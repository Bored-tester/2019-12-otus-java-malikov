package ru.otus.core.sqlmapper;

public class SqlMapperException extends RuntimeException {
    public SqlMapperException(Exception ex) {
        super(ex);
    }
}
