package com.example;

import java.io.IOException;
import java.util.List;

/**
 * JsonToSqlConverter测试类
 * 用于测试原格式和新格式的JSON转SQL功能
 */
public class JsonToSqlConverterTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("开始测试JSON转SQL转换器");
        System.out.println("========================================\n");

        // 测试原格式
        testOriginalFormat();

        System.out.println("\n========================================\n");

        // 测试新格式
        testNewFormat();

        System.out.println("\n========================================");
        System.out.println("测试完成！");
        System.out.println("========================================");
    }

    /**
     * 测试原格式JSON
     */
    private static void testOriginalFormat() {
        System.out.println("【测试1】原格式JSON（{表名: [数据数组]}）");
        System.out.println("文件路径: demo-util/demo/src/main/java/com/example/sql.json");
        System.out.println("----------------------------------------");

        try {
            String filePath = "demo-util/demo/src/main/java/com/example/sql.json";
            List<String> sqlStatements = JsonToSqlConverter.convertJsonToSql(filePath);

            if (sqlStatements.isEmpty()) {
                System.out.println("警告：未生成任何SQL语句");
            } else {
                System.out.println("成功生成 " + sqlStatements.size() + " 条SQL语句：\n");
                for (int i = 0; i < sqlStatements.size(); i++) {
                    System.out.println("SQL语句 " + (i + 1) + ":");
                    System.out.println(sqlStatements.get(i));
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.err.println("错误：读取或解析JSON文件失败");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("错误：转换过程中发生异常");
            e.printStackTrace();
        }
    }

    /**
     * 测试新格式JSON
     */
    private static void testNewFormat() {
        System.out.println("【测试2】新格式JSON（RSP.DATA[].TRADE_*）");
        System.out.println("文件路径: demo-util/demo/src/main/java/com/example/new-format-test.json");
        System.out.println("----------------------------------------");

        try {
            String filePath = "demo-util/demo/src/main/java/com/example/new-format-test.json";
            List<String> sqlStatements = JsonToSqlConverter.convertJsonToSql(filePath);

            if (sqlStatements.isEmpty()) {
                System.out.println("警告：未生成任何SQL语句");
            } else {
                System.out.println("成功生成 " + sqlStatements.size() + " 条SQL语句：\n");
                for (int i = 0; i < sqlStatements.size(); i++) {
                    System.out.println("SQL语句 " + (i + 1) + ":");
                    System.out.println(sqlStatements.get(i));
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.err.println("错误：读取或解析JSON文件失败");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("错误：转换过程中发生异常");
            e.printStackTrace();
        }
    }
}
