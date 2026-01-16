
package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonToSqlConverter {


    /**
     * 从JSON文件读取数据并生成INSERT SQL语句（批量插入格式）
     *
     * @param jsonFilePath JSON文件路径
     * @return INSERT SQL语句列表
     * @throws IOException
     */
    public static List<String> convertJsonToSql(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(jsonFilePath));

        List<String> sqlStatements = new ArrayList<>();

        // 处理对象格式的JSON，其中每个字段名是表名，值是数据数组
        if (jsonNode.isObject()) {
            // 遍历所有表名
            Iterator<String> fieldNames = jsonNode.fieldNames();
            while (fieldNames.hasNext()) {
                String tableName = fieldNames.next();
                JsonNode dataArray = jsonNode.get(tableName);

                if (dataArray.isArray()) {
                    String batchSql = generateBatchInsertStatement(dataArray, tableName);
                    sqlStatements.add(batchSql);
                }
            }
        }

        return sqlStatements;
    }

    /**
     * 生成批量INSERT语句
     *
     * @param jsonArray JSON数组节点
     * @param tableName 表名
     * @return 批量INSERT SQL语句
     */
    private static String generateBatchInsertStatement(JsonNode jsonArray, String tableName) {
        if (jsonArray.isEmpty()) {
            return "";
        }

        // 获取第一行的字段名作为列名（假设所有对象结构相同）
        JsonNode firstRecord = jsonArray.get(0);
        List<String> columnNames = getJsonFieldNames(firstRecord);

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (\n  ");

        // 添加列名
        for (int i = 0; i < columnNames.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(columnNames.get(i));
        }
        sql.append("\n) VALUES\n");

        // 添加多行值
        for (int i = 0; i < jsonArray.size(); i++) {
            if (i > 0) sql.append(",\n");
            sql.append("  (");

            JsonNode record = jsonArray.get(i);
            for (int j = 0; j < columnNames.size(); j++) {
                if (j > 0) sql.append(", ");

                JsonNode valueNode = record.get(columnNames.get(j));
                String value = formatValue(valueNode);
                sql.append(value);
            }
            sql.append(")");
        }
        sql.append(";");

        return sql.toString();
    }

    /**
     * 生成单条INSERT语句（保留原有功能）
     *
     * @param record JSON记录节点
     * @param tableName 表名
     * @return INSERT SQL语句
     */
    private static String generateSingleInsertStatement(JsonNode record, String tableName) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        boolean first = true;
        for (String fieldName : getJsonFieldNames(record)) {
            if (!first) {
                columns.append(", ");
                values.append(", ");
            }

            columns.append(fieldName);

            JsonNode valueNode = record.get(fieldName);
            String value = formatValue(valueNode);
            values.append(value);

            first = false;
        }

        return String.format("INSERT INTO %s (%s) VALUES (%s);",
                tableName, columns.toString(), values.toString());
    }

    /**
     * 获取JSON节点的所有字段名
     *
     * @param node JSON节点
     * @return 字段名列表
     */
    private static List<String> getJsonFieldNames(JsonNode node) {
        List<String> fieldNames = new ArrayList<>();
        node.fieldNames().forEachRemaining(fieldNames::add);
        return fieldNames;
    }

    /**
     * 格式化值以适应SQL语句
     *
     * @param valueNode JSON值节点
     * @return 格式化后的值字符串
     */
    private static String formatValue(JsonNode valueNode) {
        if (valueNode.isNull()) {
            return "NULL";
        } else if (valueNode.isNumber()) {
            return valueNode.asText();
        } else if (valueNode.isBoolean()) {
            return valueNode.asBoolean() ? "TRUE" : "FALSE";
        } else {
            // 对字符串值添加引号并转义特殊字符
            String value = valueNode.asText().replace("'", "''");
            return "'" + value + "'";
        }
    }

    // 示例使用方法
    public static void main(String[] args) {
        try {
            // 示例：读取JSON文件并生成SQL语句
            List<String> sqlStatements = convertJsonToSql("D:\\yudao\\myLearn\\demo-util\\demo\\src\\main\\java\\com\\example\\sql.json");

            for (String sql : sqlStatements) {
                System.out.println(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}