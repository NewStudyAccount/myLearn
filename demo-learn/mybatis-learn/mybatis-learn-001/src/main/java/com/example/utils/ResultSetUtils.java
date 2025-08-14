package com.example.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetUtils {
    
    /**
     * 将ResultSet转换为List<Map<String, Object>>
     * @param rs ResultSet对象
     * @return 数据列表
     * @throws SQLException SQL异常
     */
    public static List<Map<String, Object>> toList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        int columnCount = rs.getMetaData().getColumnCount();
        
        // 获取所有列名
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = rs.getMetaData().getColumnLabel(i);
        }
        
        // 遍历每一行数据
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String columnName : columnNames) {
                row.put(columnName, rs.getObject(columnName));
            }
            result.add(row);
        }
        
        return result;
    }
    
    /**
     * 将ResultSet转换为单个Map对象（第一行数据）
     * @param rs ResultSet对象
     * @return 第一行数据
     * @throws SQLException SQL异常
     */
    public static Map<String, Object> toSingle(ResultSet rs) throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = rs.getMetaData().getColumnLabel(i);
        }
        
        if (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String columnName : columnNames) {
                row.put(columnName, rs.getObject(columnName));
            }
            return row;
        }
        return null;
    }
}
