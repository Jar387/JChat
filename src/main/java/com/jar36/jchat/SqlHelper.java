package com.jar36.jchat;

import com.jar36.jchat.server.ServerMain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {
    private static final char SQL_INTEGER = 0x1;
    private static final char SQL_STRING = 0x2;
    private static char getSqlCompatability(Field field) throws SQLException {
        Class<?> fieldType = field.getType();
        if(fieldType==int.class||
                fieldType==char.class||
                fieldType==short.class||
                fieldType==long.class){
            return SQL_INTEGER;
        } else if (fieldType==String.class) {
            return SQL_STRING;
        }else {
            throw new SQLException("Unsupported data type to sql: "+field.getType().getName());
        }
    }
    public static void createTable(Statement statement, Class<?> clazz) throws SQLException {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(clazz.getSimpleName()).append(" (");
        boolean first = true;
        for(Field f : fields) {
            if (f.isAnnotationPresent(Data.class)) {
                // this field is a sql data
                if (getSqlCompatability(f) == SQL_INTEGER) {
                    sb.append(f.getName()).append(" INTEGER ");
                } else {
                    sb.append(f.getName()).append(" TEXT ");
                }
                if (first) {
                    sb.append("PRIMARY KEY");
                    first = false;
                }
                sb.append(',');
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");
        statement.execute(sb.toString());
    }
    public static <T, N extends Number> T getTableObject(Statement statement, Class<T> clazz, N key) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(clazz.getSimpleName()).append(" WHERE ");
        Field[] fields = clazz.getDeclaredFields();
        sb.append(fields[0].getName()).append(" = ").append(key.toString()).append(';');
        ServerMain.logger.info(sb.toString());
        ResultSet resultSet = statement.executeQuery(sb.toString());
        T result = clazz.getDeclaredConstructor().newInstance();
        for(Field f : fields){
            f.setAccessible(true);
            if(getSqlCompatability(f)==SQL_INTEGER){
                f.set(result, resultSet.getInt(f.getName()));
            }else {
                f.set(result, resultSet.getString(f.getName()));
            }
        }
        return result;
    }
    public static <T> T getTableObject(Statement statement, Class<T> clazz, String key) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(clazz.getSimpleName()).append(" WHERE ");
        Field[] fields = clazz.getDeclaredFields();
        sb.append(fields[0].getName()).append(" = \"").append(key).append("\";");
        ResultSet resultSet = statement.executeQuery(sb.toString());
        T result = clazz.getDeclaredConstructor().newInstance();
        for(Field f : fields){
            f.setAccessible(true);
            if(getSqlCompatability(f)==SQL_INTEGER){
                f.set(result, resultSet.getInt(f.getName()));
            }else {
                f.set(result, resultSet.getString(f.getName()));
            }
        }
        return result;
    }
}
