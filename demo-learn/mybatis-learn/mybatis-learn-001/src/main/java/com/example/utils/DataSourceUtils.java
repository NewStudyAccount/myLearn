package com.example.utils;

import java.sql.*;

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
    public void executeQuery(String sql, ResultSetHandler handler) throws SQLException {
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

            //4、执行sql
            resultSet = preparedStatement.executeQuery();

            //5、处理结果
            handler.handle(resultSet);
        } catch (Exception e) {
            throw new SQLException("Database operation failed", e);
        } finally {
            //6、关闭资源
            if (resultSet != null) {
                try { resultSet.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (preparedStatement != null) {
                try { preparedStatement.close(); } catch (SQLException e) { /* ignore */ }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* ignore */ }
            }
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
