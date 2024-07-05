package com.jar36.jchat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {
    private static final char NOT_SQL = 0x0;
    private static final char SQL_INTEGER = 0x1;
    private static final char SQL_STRING = 0x2;

    private static char getSqlCompatability(Field field) throws SQLException {
        if (!field.isAnnotationPresent(Data.class)) {
            return NOT_SQL;
        }
        Class<?> fieldType = field.getType();
        if (fieldType == int.class ||
                fieldType == char.class ||
                fieldType == short.class ||
                fieldType == long.class) {
            return SQL_INTEGER;
        } else if (fieldType == String.class) {
            return SQL_STRING;
        } else {
            throw new SQLException("Unsupported data type to sql: " + field.getType().getName());
        }
    }

    private static boolean isPrimary(Field field) {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

    public static void createTable(Statement statement, Class<?> clazz) throws SQLException {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(clazz.getSimpleName()).append(" (");
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (sqlComaptability == SQL_INTEGER) {
                sb.append(f.getName()).append(" INTEGER ");
            } else {
                sb.append(f.getName()).append(" TEXT ");
            }
            if (isPrimary(f)) {
                sb.append("PRIMARY KEY");
            }
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");
        statement.execute(sb.toString());
    }

    public static <T, N extends Number> T queryTableToObject(Statement statement, Class<T> clazz, String key, N value) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(clazz.getSimpleName()).append(" WHERE ");
        Field[] fields = clazz.getDeclaredFields();
        sb.append(key).append(" = ").append(value.toString()).append(';');
        ResultSet resultSet = statement.executeQuery(sb.toString());
        if (!resultSet.next()) {
            return null;
        }
        T result = clazz.getDeclaredConstructor().newInstance();
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            f.setAccessible(true);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (sqlComaptability == SQL_INTEGER) {
                f.set(result, resultSet.getInt(f.getName()));
            } else {
                f.set(result, resultSet.getString(f.getName()));
            }
        }
        return result;
    }

    public static <T> T queryTableToObject(Statement statement, Class<T> clazz, String key, String value) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(clazz.getSimpleName()).append(" WHERE ");
        Field[] fields = clazz.getDeclaredFields();
        sb.append(key).append(" = \"").append(value).append("\";");
        ResultSet resultSet = statement.executeQuery(sb.toString());
        if (!resultSet.next()) {
            return null;
        }
        T result = clazz.getDeclaredConstructor().newInstance();
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            f.setAccessible(true);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (sqlComaptability == SQL_INTEGER) {
                f.set(result, resultSet.getInt(f.getName()));
            } else {
                f.set(result, resultSet.getString(f.getName()));
            }
        }
        return result;
    }

    public static void insertObject(Statement statement, Class<?> clazz, Object object) throws SQLException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(clazz.getSimpleName()).append(" VALUES (");
        Field[] fields = object.getClass().getDeclaredFields();
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            f.setAccessible(true);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (sqlComaptability == SQL_INTEGER) {
                sb.append(f.getInt(object));
            } else {
                sb.append('\"').append(f.get(object)).append('\"');
            }
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");
        statement.execute(sb.toString());
    }

    public static void updateObject(Statement statement, Class<?> clazz, Object object) throws SQLException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(clazz.getSimpleName()).append(" SET ");
        Field[] fields = clazz.getDeclaredFields();
        String primary = null;
        String value1 = null;
        int value2 = 0;
        boolean choice = false;
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            f.setAccessible(true);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (isPrimary(f)) {
                primary = f.getName();
                if (sqlComaptability == SQL_INTEGER) {
                    value2 = f.getInt(object);
                    choice = true;
                } else {
                    value1 = (String) f.get(object);
                }
            }
            sb.append(f.getName()).append(" = ");
            if (sqlComaptability == SQL_INTEGER) {
                sb.append(f.getInt(object));
            } else {
                sb.append('\"').append(f.get(object)).append('\"');
            }
            sb.append(',');
        }
        if (primary == null) {
            throw new SQLException("cannot find primary key in sql class " + clazz.getSimpleName());
        }
        sb.deleteCharAt(sb.length() - 1).append(" WHERE ").append(primary).append(" = ");
        if (choice) {
            sb.append(value2);
        } else {
            sb.append(value1);
        }
        sb.append(';');
        statement.execute(sb.toString());
    }

    public static void deleteObject(Statement statement, Class<?> clazz, String value) throws SQLException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(clazz.getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        String primary = null;
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            f.setAccessible(true);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (isPrimary(f)) {
                primary = f.getName();
            }
        }
        sb.append(" WHERE ").append(primary).append(" = \"").append(value).append('\"');
        sb.append(';');
        statement.execute(sb.toString());
    }

    public static void deleteObject(Statement statement, Class<?> clazz, int value) throws SQLException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(clazz.getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        String primary = null;
        char sqlComaptability;
        for (Field f : fields) {
            sqlComaptability = getSqlCompatability(f);
            f.setAccessible(true);
            if (sqlComaptability == NOT_SQL) {
                continue;
            }
            if (isPrimary(f)) {
                primary = f.getName();
            }
        }
        sb.append(" WHERE ").append(primary).append(" = ").append(value);
        sb.append(';');
        statement.execute(sb.toString());
    }
}
