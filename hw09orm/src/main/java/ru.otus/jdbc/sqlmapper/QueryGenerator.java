package ru.otus.jdbc.sqlmapper;

import ru.otus.jdbc.sqlmapper.model.MetaObject;

import java.util.stream.Collectors;

public class QueryGenerator {
    private static final String SELECT_BY_ID_TEMPLATE = "select %s from %s where %s = ?";
    private static final String INSERT_TEMPLATE = "insert into %s(%s) values (%s)";

    public static String getSelectByIdQuery(MetaObject metaObject) {
        StringBuilder fieldNameList = new StringBuilder(metaObject.getId().getFieldName());
        if (metaObject.getNonIdFields().size() > 0) {
            fieldNameList.append(", ");
            fieldNameList.append(metaObject.getNonIdFieldNames());
        }
        return String.format(SELECT_BY_ID_TEMPLATE, fieldNameList.toString(), metaObject.getTableName(), metaObject.getId().getFieldName());
    }

    public static String getInsertQuery(MetaObject metaObject) {
        String fieldNameList = metaObject.getNonIdFieldNames();
        String valuesList = metaObject.getNonIdFieldValues().stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        return String.format(INSERT_TEMPLATE, metaObject.getTableName(), fieldNameList, valuesList);
    }
}
