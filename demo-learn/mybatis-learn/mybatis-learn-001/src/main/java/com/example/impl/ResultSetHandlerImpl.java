package com.example.impl;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ResultSetHandlerImpl<T> implements ResultInterface<T> {

    @Override
    public void handle(ResultSet rs) throws SQLException {
        System.out.println("ResultSetHandlerImpl"+rs);

    }

    @Override
    public T handleObject(ResultSet rs, Class<T> clazz) throws SQLException {

        try {
            if (rs.next()) {
                // 创建实例
                T instance = clazz.getDeclaredConstructor().newInstance();

                // 获取类的所有字段
                Field[] fields = clazz.getDeclaredFields();

                // 遍历字段并设置值
                for (Field field : fields) {
                    field.setAccessible(true); // 允许访问私有字段

                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();

                    try {
                        // 根据字段类型设置相应的值
                        if (fieldType.equals(String.class)) {
                            field.set(instance, rs.getString(fieldName));
                        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                            field.set(instance, rs.getInt(fieldName));
                        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                            field.set(instance, rs.getLong(fieldName));
                        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                            field.set(instance, rs.getDouble(fieldName));
                        } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                            field.set(instance, rs.getFloat(fieldName));
                        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                            field.set(instance, rs.getBoolean(fieldName));
                        } else if (fieldType.equals(java.util.Date.class)) {
                            field.set(instance, rs.getDate(fieldName));
                        } else if (fieldType.equals(java.sql.Date.class)) {
                            field.set(instance, rs.getDate(fieldName));
                        } else if (fieldType.equals(java.sql.Timestamp.class)) {
                            field.set(instance, rs.getTimestamp(fieldName));
                        } else {
                            // 对于其他类型，尝试直接获取对象
                            field.set(instance, rs.getObject(fieldName));
                        }
                    } catch (Exception e) {
                        // 如果字段在结果集中不存在，忽略该字段
                        continue;
                    }
                }

                return instance;
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error while mapping ResultSet to object", e);
        }
    }


    // 处理对象列表的方法
    public List<T> handleList(ResultSet rs, Class<T> clazz) throws SQLException {
        java.util.List<T> list = new java.util.ArrayList<>();
        try {
            while (rs.next()) {
                // 创建实例
                T instance = clazz.getDeclaredConstructor().newInstance();

                // 获取类的所有字段
                Field[] fields = clazz.getDeclaredFields();

                // 遍历字段并设置值
                for (Field field : fields) {
                    field.setAccessible(true); // 允许访问私有字段

                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();

                    try {
                        // 根据字段类型设置相应的值
                        if (fieldType.equals(String.class)) {
                            field.set(instance, rs.getString(fieldName));
                        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                            field.set(instance, rs.getInt(fieldName));
                        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                            field.set(instance, rs.getLong(fieldName));
                        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                            field.set(instance, rs.getDouble(fieldName));
                        } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                            field.set(instance, rs.getFloat(fieldName));
                        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                            field.set(instance, rs.getBoolean(fieldName));
                        } else if (fieldType.equals(java.util.Date.class)) {
                            field.set(instance, rs.getDate(fieldName));
                        } else if (fieldType.equals(java.sql.Date.class)) {
                            field.set(instance, rs.getDate(fieldName));
                        } else if (fieldType.equals(java.sql.Timestamp.class)) {
                            field.set(instance, rs.getTimestamp(fieldName));
                        } else {
                            // 对于其他类型，尝试直接获取对象
                            field.set(instance, rs.getObject(fieldName));
                        }
                    } catch (Exception e) {
                        // 如果字段在结果集中不存在，忽略该字段
                        continue;
                    }
                }

                list.add(instance);
            }
            return list;
        } catch (Exception e) {
            throw new SQLException("Error while mapping ResultSet to object list", e);
        }
    }
}
