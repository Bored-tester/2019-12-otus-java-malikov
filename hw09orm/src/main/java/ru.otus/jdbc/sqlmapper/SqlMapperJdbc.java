package ru.otus.jdbc.sqlmapper;

import ru.otus.core.dao.EntityDaoException;
import ru.otus.core.sqlmapper.SqlMapper;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sqlmapper.metaObject.MetaObjectParser;
import ru.otus.jdbc.sqlmapper.metaObject.MetaObjectUpdater;
import ru.otus.jdbc.sqlmapper.metaObject.model.MetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class SqlMapperJdbc<T> implements SqlMapper<T> {
    private final DbExecutor<T> dbExecutor;
    String insertQuery = null;
    String selectQuery = null;

    public SqlMapperJdbc(DbExecutor<T> dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    @Override
    public long insertRecord(Connection connection, T objectToInsert) {
        MetaObject metaObjectToInsert = MetaObjectParser.parseObjectForSqlGeneration(objectToInsert);
        if (insertQuery == null) {
            insertQuery = QueryGenerator.getInsertQuery(metaObjectToInsert);
        }
        List<String> valuesToInsert = metaObjectToInsert.getNonIdFieldValues();
        try {
            long id = dbExecutor.insertRecord(connection, insertQuery, valuesToInsert);
            Field idField = objectToInsert.getClass().getDeclaredField(metaObjectToInsert.getId().getFieldName());
            MetaObjectUpdater.setId(id, idField, objectToInsert);
            return id;
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e);
            throw new EntityDaoException(e);
        }
    }

    @Override
    public Optional<T> selectRecord(Connection connection, Class<T> clazz, long id) {
        MetaObject metaObjectToSelect = MetaObjectParser.parseClassForSqlGeneration(clazz);
        if (selectQuery == null) {
            selectQuery = QueryGenerator.getSelectByIdQuery(metaObjectToSelect);
        }
        try {
            return dbExecutor.selectRecord(connection, selectQuery, id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return (T) SelectResultDeserializer.deserializeObjectFromSelectResult(clazz, resultSet);
                    }
                } catch (Exception e) {
                    System.out.println(String.format("Error! Failed to deserialize result of select %s:\n%s\ninto class %s\nException: %s", selectQuery, resultSet, clazz.getName(), e.getMessage()));
                }
                return null;
            });
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e);
        }
        return Optional.empty();
    }
}
