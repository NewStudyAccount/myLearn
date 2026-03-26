package com.example;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonToSqlConverterTest {

    @Test
    public void testConvertJsonToSql() throws IOException {
        // 测试JSON文件路径
        String jsonFilePath = "src/main/java/com/example/test_data.json";
        
        // 调用转换方法
        List<String> sqlStatements = JsonToSqlConverter.convertJsonToSql(jsonFilePath);
        
        // 验证结果
        assertNotNull(sqlStatements);
        assertFalse(sqlStatements.isEmpty());
        
        // 打印生成的SQL语句
        System.out.println("Generated SQL Statements:");
        System.out.println("========================");
        for (String sql : sqlStatements) {
            System.out.println(sql);
            System.out.println("------------------------");
        }
        
        // 验证包含了建表语句和插入语句
        boolean hasCreateTable = false;
        boolean hasInsert = false;
        
        for (String sql : sqlStatements) {
            if (sql.contains("CREATE TABLE IF NOT EXISTS")) {
                hasCreateTable = true;
            }
            if (sql.contains("INSERT INTO")) {
                hasInsert = true;
            }
        }
        
        assertTrue(hasCreateTable, "应该包含CREATE TABLE语句");
        assertTrue(hasInsert, "应该包含INSERT语句");
    }
}