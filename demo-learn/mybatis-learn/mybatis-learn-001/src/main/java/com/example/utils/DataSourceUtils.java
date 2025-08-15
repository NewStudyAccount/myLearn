package com.example.utils;

import java.sql.*;
import java.util.Map;
import java.util.Set;

public class DataSourceUtils {

    String url = "jdbc:mysql://192.168.200.128:3306/mybatis_demo?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true";
    String username = "root";
    String password = "123456";
    // 添加多个驱动类名以兼容不同版本
    String[] driverClassNames = {
            "com.mysql.cj.jdbc.Driver",     // MySQL 8.0+
            "com.mysql.jdbc.Driver"         // MySQL 5.7 及更早版本
    };

    /**
     * 改进版本：执行查询并处理结果
     */
    /**
     * 改进版本：执行查询并处理结果
     */
    public void executeQuery(String sql, ResultSetHandler handler, Map<String, Object> queryParams) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //1、加载驱动（尝试多个驱动类）
            loadDriver();

            //2、建立连接
            connection = DriverManager.getConnection(url, username, password);

            //3、创建 PreparedStatement 预提交sql
            preparedStatement = connection.prepareStatement(sql);

            //4、设置参数
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    String paramName = entry.getKey();
                    Object paramValue = entry.getValue();

                    try {
                        if (paramName.matches("\\d+")) {
                            int position = Integer.parseInt(paramName);
                            setParameter(preparedStatement, position, paramValue);
                        } else {
                            throw new SQLException("Named parameters require SQL parsing, not implemented");
                        }
                    } catch (NumberFormatException e) {
                        throw new SQLException("Invalid parameter position: " + paramName);
                    }
                }
            }

            //5、执行sql
            resultSet = preparedStatement.executeQuery();

            //6、处理结果
            handler.handle(resultSet);
        } catch (Exception e) {
            throw new SQLException("Database operation failed", e);
        } finally {
            //7、关闭资源
            closeQuietly(resultSet);
            closeQuietly(preparedStatement);
            closeQuietly(connection);
        }
    }



    /**
     *
     * 设置的查询参数也要和sql语句中的参数名顺序一致
     *
     */
    public void executeQuery2(String sql, ResultSetHandler handler, Map<String, String> queryParams) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            loadDriver();
            connection = DriverManager.getConnection(url, username, password);

            // 解析命名参数并替换为位置参数
            String parsedSql = parseNamedParameters(sql, queryParams.keySet());
            preparedStatement = connection.prepareStatement(parsedSql);

            // 设置参数值
            int index = 1;
            for (String paramName : queryParams.keySet()) {
                preparedStatement.setString(index++, queryParams.get(paramName));
            }

            resultSet = preparedStatement.executeQuery();
            handler.handle(resultSet);
        } catch (Exception e) {
            throw new SQLException("Database operation failed", e);
        } finally {
            // 关闭资源
            closeResources(resultSet, preparedStatement, connection);
        }
    }

    private String parseNamedParameters(String sql, Set<String> paramNames) {
        // 简单实现：将 :paramName 替换为 ?
        for (String paramName : paramNames) {
            sql = sql.replace(":" + paramName, "?");
        }
        return sql;
    }

    // 辅助方法：安全关闭资源
    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 忽略关闭异常
            }
        }
    }

    // 辅助方法：根据参数类型设置参数
    private void setParameter(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.OTHER);
        } else if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (Long) value);
        } else if (value instanceof Double) {
            ps.setDouble(index, (Double) value);
        } else if (value instanceof Boolean) {
            ps.setBoolean(index, (Boolean) value);
        } else if (value instanceof Date) {
            ps.setDate(index, (Date) value);
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else {
            ps.setObject(index, value);
        }
    }

    /**
     * 加载合适的 JDBC 驱动
     */
    private void loadDriver() throws SQLException {
        for (String driverClassName : driverClassNames) {
            try {
                Class.forName(driverClassName);
                System.out.println("Loaded JDBC driver: " + driverClassName);
                return;
            } catch (ClassNotFoundException e) {
                // 继续尝试下一个驱动
                System.out.println("Failed to load driver: " + driverClassName);
            }
        }
        throw new SQLException("No suitable JDBC driver found");
    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection() throws SQLException {
        try {
            loadDriver();
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new SQLException("Failed to get database connection", e);
        }
    }

    /**
     * 结果集处理接口
     */
    public interface ResultSetHandler {
        void handle(ResultSet rs) throws SQLException;
    }
}
