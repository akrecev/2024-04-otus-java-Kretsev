package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String tableName;
    private final String idFieldName;
    private final String fields;
    private final String params;
    private final String fieldsWithoutId;
    private final String paramsWithoutId;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        this.tableName = metaData.getName().toLowerCase();
        this.idFieldName = metaData.getIdField().getName();
        this.fields = metaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", "));
        this.params = metaData.getAllFields().stream().map(f -> "?").collect(Collectors.joining(", "));
        this.fieldsWithoutId =
                metaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(", "));
        this.paramsWithoutId =
                metaData.getFieldsWithoutId().stream().map(f -> "?").collect(Collectors.joining(", "));
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?", tableName, idFieldName);
    }

    @Override // insert into test(id, name) values (?, ?)
    public String getInsertSql() {
        return String.format("insert into %s(%s) values (%s)", tableName, fieldsWithoutId, paramsWithoutId);
    }

    @Override // update client set name = ? where id = ?
    public String getUpdateSql() {
        return String.format("update %s set (%s) = (%s) where %s = ?", tableName, fields, params, idFieldName);
    }
}
