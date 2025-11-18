package com.codewithancho.api.storage;

import com.codewithancho.api.core.annotations.Column;
import com.codewithancho.api.core.annotations.PrimaryKey;
import com.codewithancho.api.core.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseMapper<T> {

    private final Connection connection;
    private final Class<T> clazz;
    private String table;

    private String key;
    private final LinkedHashMap<String, Field> values;

    public DatabaseMapper(Connection connection, Class<T> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        this.values = new LinkedHashMap<>();

        initReflection();
    }

    /**
     * Stores all the method that that type<br>
     * has stored to be saved or loaded inside of the<br>
     * database/table that was provided
     */
    public void initReflection() {

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new Error("@Table annotation is not present in class " + clazz.getName());
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(PrimaryKey.class)) {

                Column col = field.getAnnotation(Column.class);
                if (col == null) {
                    throw new Error("@Column annotation is not present in class " + clazz.getName());
                }

                if (this.key != null) {
                    throw new Error("@PrimaryKey key annotation is already present in class " + clazz.getName());
                }

                this.key = col.name();
            }

            if (field.isAnnotationPresent(Table.class)) {

                Column col = field.getAnnotation(Column.class);
                if (col == null) {
                    throw new Error("@Column annotation is not present in class " + clazz.getName());
                }

                if (this.table != null) {
                    throw new Error("@Table annotation is already present in class " + clazz.getName());
                }

                this.table = col.name();
            }

            if(field.isAnnotationPresent(Column.class)) {
                Column col = field.getAnnotation(Column.class);
                values.put(col.name(), field);
            }
        }

        if (this.key == null) {
            throw new Error("@PrimaryKey annotation is not present in class " + clazz.getName());
        }

        if (this.table == null) {
            throw new Error("@Table annotation is not present in class " + clazz.getName());
        }
    }

    /**
     *
     * @param rs The result set that has to be parsed and looked from the database
     * @return A perfectly mapped Object that can later be used or stored.
     */
    public T mapRowToObject(ResultSet rs) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T obj = clazz.getDeclaredConstructor().newInstance();

        values.forEach((s, field) -> {
            try {
                Object sqlValue = rs.getObject(s);
                field.set(obj, sqlValue);
            } catch (SQLException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return obj;
    }

    /**
     *
     * @return Loads all the objects that you want it to, from the database and table that it was provided.
     */
    public List<T> loadAll() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();

        String sql = "SELECT * FROM " + table;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T obj = mapRowToObject(resultSet);
                results.add(obj);
            }
        }

        return  results;
    }

    /**
     *
     * @param obj If the object doesn't exist, insert it into the database.
     */
    public void insert(T obj) throws SQLException, IllegalAccessException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
        for (String name : values.keySet()) {
            sql.append(name).append(",");
        }
        sql.append(") VALUES (");

        for (Field field : values.values()) {
            sql.append("?").append(",");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(");");

        int i = 1;

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (Field f : values.values()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Column.class)) {
                    Column column = f.getAnnotation(Column.class);
                    if (column == null) {
                        throw new Error("@Column annotation is not present in class " + clazz.getName());
                    }

                    Object fieldObj =  f.get(obj);
                    ps.setObject(i++, fieldObj);
                }
            }
            ps.executeUpdate();
        }
    }

    /**
     *
     * @param id The parameter that is needed in order to find the Object that we're looking for.
     * @return If found, return the Object that was found with the data that was extracted from the database.
     */
    public T find(Object id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "SELECT * FROM " + table + " WHERE " + key + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            return mapRowToObject(resultSet);
        }
    }

    /**
     * @param obj What object you want to exactly update. <br>
     * Update an already existing entry that is in the database.
     */
    public void update(T obj) throws SQLException, IllegalAccessException {
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");

        for(String name : values.keySet()) {
            if (name.equals(key)) continue;
            sql.append(name).append(" = ?,");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" WHERE " + key + " = ?");

        Field pkField = values.get(key);
        Object pkValue = pkField.get(obj);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            AtomicInteger i = new AtomicInteger(1);
            values.forEach((name, field) -> {
                try {
                    if(key.equals(name)) {
                        return;
                    }
                    Object value =  field.get(obj);
                    ps.setObject(i.getAndIncrement(), value);
                } catch (SQLException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            ps.setObject(i.get(), pkValue);
            ps.executeUpdate();
        }
    }

    /**
     *
     * @param obj Pass in the object that you want to save to the database.
     */
    public void save(T obj) throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Field pkField = values.get(key);
        Object pkValue = pkField.get(obj);

        T exists = find(pkValue);
        if (exists == null) {
            insert(obj);
            return;
        }

        update(obj);
    }

    /**
     *
     * @param id Give me the ID of the entry that you'd like me to delete
     * @throws SQLException Incase something goes wrong, throw an exception to the console.
     */
    public void delte(Object id) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + key + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    /**
     *
     * @param id ID of the entry, that is used to check whether it's saved or not.
     * @return Whether the entry exists in the database
     * @throws SQLException In case something goes wrong, throw an error into the console
     */
    public boolean exists(Object id) throws SQLException {
        String  sql = "SELECT * FROM " + table + " WHERE " + key + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param column Colum that holds the value
     * @param value What should be the value of the entry that's supposed to be deleted.
     * @throws SQLException In case something goes wrong, throw an error in the console, for example if the connection gets broken.
     */
    public void deleteWhere(String column, Object value) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, value);
            ps.executeUpdate();
        }
    }
}
