package com.example;

import java.io.IOException;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        try {
            System.out.println("=== JSON转SQL转换器测试 ===\n");
            
            // 测试文件路径
            String jsonFilePath = "src/main/java/com/example/test_data.json";
            
            // 调用转换方法
            List<String> sqlStatements = JsonToSqlConverter.convertJsonToSql(jsonFilePath);
            
            // 显示结果
            System.out.println("生成的SQL语句：");
            System.out.println("================");
            
            int statementIndex = 1;
            for (String sql : sqlStatements) {
                System.out.println("语句 " + statementIndex + ":");
                System.out.println(sql);
                System.out.println("--------------------\n");
                statementIndex++;
            }
            
            System.out.println("总共生成了 " + sqlStatements.size() + " 条SQL语句");
            
            // 统计各类语句数量
            int createTableCount = 0;
            int insertCount = 0;
            
            for (String sql : sqlStatements) {
                if (sql.contains("CREATE TABLE IF NOT EXISTS")) {
                    createTableCount++;
                } else if (sql.contains("INSERT INTO")) {
                    insertCount++;
                }
            }
            
            System.out.println("其中：");
            System.out.println("- CREATE TABLE 语句: " + createTableCount + " 条");
            System.out.println("- INSERT 语句: " + insertCount + " 条");
            
        } catch (IOException e) {
            System.err.println("处理文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}