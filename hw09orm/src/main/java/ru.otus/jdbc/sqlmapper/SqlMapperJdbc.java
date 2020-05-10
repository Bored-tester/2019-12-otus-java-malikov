package ru.otus.jdbc.sqlmapper;

import lombok.AllArgsConstructor;
import ru.otus.core.dao.EntityDaoException;
import ru.otus.core.sqlmapper.SqlMapper;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sqlmapper.model.MetaObject;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class SqlMapperJdbc<T> implements SqlMapper<T> {
    private final DbExecutor<T> dbExecutor;

    @Override
    public long insertRecord(Connection connection, T objectToInsert) {
        MetaObject metaObjectToInsert = MetaObjectParser.parseObjectForSqlGeneration(objectToInsert);
        String query = QueryGenerator.getInsertQuery(metaObjectToInsert);
        List<String> valuesToInsert = metaObjectToInsert.getNonIdFieldValues();
        try {
            return dbExecutor.insertRecord(connection, query, valuesToInsert);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e);
            throw new EntityDaoException(e);
        }
    }

    @Override
    public Optional<T> selectRecord(Connection connection, Class<T> clazz, long id) {
        MetaObject metaObjectToSelect = MetaObjectParser.parseClassForSqlGeneration(clazz);
        String query = QueryGenerator.getSelectByIdQuery(metaObjectToSelect);
        try {
            return dbExecutor.selectRecord(connection, query, id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        return (T) SelectResultDeserializer.deserializeObjectFromSelectResult(clazz, resultSet);
                    }
                } catch (Exception e) {
                    System.out.println(String.format("Error! Failed to deserialize result of select %s:\n%s\ninto class %s\nException: %s", query, resultSet, clazz.getName(), e.getMessage()));
                }
                return null;
            });
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e);
        }
        return Optional.empty();
    }
}
