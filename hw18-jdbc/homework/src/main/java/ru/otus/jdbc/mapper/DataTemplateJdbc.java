package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return getEntity(resultSet);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), resultSet -> {
                    var entities = new ArrayList<T>();
                    try {
                        while (resultSet.next()) {
                            entities.add(getEntity(resultSet));
                        }
                        return entities;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected exception"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            String insert = entitySQLMetaData.getInsertSql();
            List<Object> params = getParamsWithoutId(client);
            return dbExecutor.executeStatement(connection, insert, params);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getParamsWithoutId(client));
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    private T getEntity(ResultSet resultSet) throws SQLException {
        T entity;
        try {
            List<Object> args = new ArrayList<>();
            for (Field field : entityClassMetaData.getAllFields()) {
                args.add(resultSet.getObject(field.getName()));
            }
            entity = entityClassMetaData.getConstructor().newInstance(args.toArray());
        } catch (InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
        return entity;
    }

    private List<Object> getParamsWithoutId(T entity) throws IllegalAccessException {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> params = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            params.add(field.get(entity));
        }

        return params;
    }
}
