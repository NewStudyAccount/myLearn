
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
     * 从JSON文件读取数据并生成INSERT SQL语句(批量插入格式)
     *
     * @param jsonFilePath JSON文件路径
     * @return INSERT SQL语句列表
     * @throws IOException
     */
    public static List<String> convertJsonToSql(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File(jsonFilePath));

        List<String> sqlStatements = new ArrayList<>();

        // 处理对象格式的JSON
        if (jsonNode.isObject()) {
            // 检查是否是新格式（包含RSP.DATA结构）
            if (jsonNode.has("RSP") && jsonNode.get("RSP").has("DATA")) {
                // 新格式：RSP.DATA[].TRADE_*
                sqlStatements.addAll(processNestedFormat(jsonNode));
            } else {
                // 原格式：{表名: [数据数组]}
                sqlStatements.addAll(processOriginalFormat(jsonNode));
            }
        }

        return sqlStatements;
    }

    /**
     * 处理原始格式的JSON数据
     *
     * @param jsonNode JSON根节点
     * @return SQL语句列表
     */
    private static List<String> processOriginalFormat(JsonNode jsonNode) {
        List<String> sqlStatements = new ArrayList<>();
        
        // 遍历所有表名
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()) {
            String tableName = fieldNames.next();
            JsonNode dataArray = jsonNode.get(tableName);

            if (dataArray.isArray()) {
                // 先生成建表语句
                String createTableSql = generateCreateTableStatement(dataArray, tableName);
                if (!createTableSql.isEmpty()) {
                    sqlStatements.add(createTableSql);
                }
                
                // 再生成插入语句
                String batchSql = generateBatchInsertStatement(dataArray, tableName);
                if (!batchSql.isEmpty()) {
                    sqlStatements.add(batchSql);
                }
            }
        }
        
        return sqlStatements;
    }

    /**
     * 处理嵌套格式的JSON数据（新格式）
     *
     * @param jsonNode JSON根节点
     * @return SQL语句列表
     */
    private static List<String> processNestedFormat(JsonNode jsonNode) {
        List<String> sqlStatements = new ArrayList<>();
        
        JsonNode dataArray = jsonNode.get("RSP").get("DATA");
        
        if (dataArray.isArray()) {
            // 遍历DATA数组中的每个元素
            for (JsonNode dataItem : dataArray) {
                // 遍历每个dataItem中的所有字段
                Iterator<String> fieldNames = dataItem.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    JsonNode fieldValue = dataItem.get(fieldName);
                    
                    // 只处理以"TRADE_"开头的字段且值为数组的情况
                    if (fieldName.startsWith("TRADE_") && fieldValue.isArray()) {
                        // 先生成建表语句
                        String createTableSql = generateCreateTableStatement(fieldValue, fieldName);
                        if (!createTableSql.isEmpty()) {
                            sqlStatements.add(createTableSql);
                        }
                        
                        // 再生成插入语句
                        String batchSql = generateBatchInsertStatement(fieldValue, fieldName);
                        if (!batchSql.isEmpty()) {
                            sqlStatements.add(batchSql);
                        }
                    }
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
     * 生成CREATE TABLE IF NOT EXISTS语句
     *
     * @param jsonArray JSON数组节点
     * @param tableName 表名
     * @return CREATE TABLE SQL语句
     */
    private static String generateCreateTableStatement(JsonNode jsonArray, String tableName) {
        if (jsonArray.isEmpty()) {
            return "";
        }

        // 获取第一行的字段名和类型
        JsonNode firstRecord = jsonArray.get(0);
        List<String> columnDefinitions = new ArrayList<>();

        Iterator<String> fieldNames = firstRecord.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode valueNode = firstRecord.get(fieldName);

            // 推断字段类型
            String fieldType = inferFieldType(valueNode);
            columnDefinitions.add(fieldName + " " + fieldType);
        }

        // 拼接CREATE TABLE语句
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n  ");
        for (int i = 0; i < columnDefinitions.size(); i++) {
            if (i > 0) {
                sql.append(",\n  ");
            }
            sql.append(columnDefinitions.get(i));
        }
        sql.append("\n);");

        return sql.toString();
    }

    /**
     * 推断字段类型
     *
     * @param valueNode JSON值节点
     * @return 数据库字段类型
     */
    private static String inferFieldType(JsonNode valueNode) {
        if (valueNode.isNull()) {
            return "TEXT"; // 默认文本类型
        } else if (valueNode.isNumber()) {
            return valueNode.isIntegralNumber() ? "INT" : "DECIMAL";
        } else if (valueNode.isBoolean()) {
            return "BOOLEAN";
        } else {
            return "VARCHAR(255)"; // 字符串默认长度
        }
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
