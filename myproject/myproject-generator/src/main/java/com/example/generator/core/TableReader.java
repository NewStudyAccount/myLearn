package com.example.generator.core;

import com.example.generator.domain.ColumnInfo;
import com.example.generator.domain.TableInfo;
import com.example.generator.enums.TypeMappingEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TableReader {

    private static final Logger log = LoggerFactory.getLogger(TableReader.class);

    private final JdbcTemplate jdbcTemplate;

    public TableReader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TableInfo readTable(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);

        String tableCommentSql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
        try {
            String comment = jdbcTemplate.queryForObject(tableCommentSql, String.class, tableName);
            tableInfo.setTableComment(comment != null ? comment : "");
        } catch (Exception e) {
            log.warn("Could not get table comment for: {}", tableName);
        }

        List<ColumnInfo> columns = jdbcTemplate.query(
                getColumnsSql(tableName),
                new ColumnRowMapper(),
                tableName
        );

        tableInfo.setColumns(columns);

        for (ColumnInfo column : columns) {
            if (column.isPrimaryKey()) {
                tableInfo.setPrimaryKey(column);
                break;
            }
        }

        return tableInfo;
    }

    public List<String> getAllTables() {
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_TYPE = 'BASE TABLE'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    private String getColumnsSql(String tableName) {
        return "SELECT " +
                "COLUMN_NAME, " +
                "COLUMN_COMMENT, " +
                "COLUMN_TYPE, " +
                "DATA_TYPE, " +
                "IS_NULLABLE, " +
                "COLUMN_KEY, " +
                "EXTRA, " +
                "COLUMN_DEFAULT, " +
                "CHARACTER_MAXIMUM_LENGTH, " +
                "NUMERIC_PRECISION, " +
                "NUMERIC_SCALE " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? " +
                "ORDER BY ORDINAL_POSITION";
    }

    private static class ColumnRowMapper implements RowMapper<ColumnInfo> {

        @Override
        public ColumnInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ColumnInfo column = new ColumnInfo();

            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setColumnComment(rs.getString("COLUMN_COMMENT"));
            column.setColumnType(rs.getString("COLUMN_TYPE"));
            column.setDataType(rs.getString("DATA_TYPE"));
            column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));

            String columnKey = rs.getString("COLUMN_KEY");
            column.setPrimaryKey("PRI".equals(columnKey));

            String extra = rs.getString("EXTRA");
            column.setAutoIncrement(extra != null && extra.contains("auto_increment"));

            column.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
            column.setColumnSize(rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
            column.setDecimalDigits(rs.getInt("NUMERIC_SCALE"));

            String fieldName = toCamelCase(column.getColumnName());
            column.setFieldName(fieldName);
            column.setFieldNameCapitalized(capitalize(fieldName));

            String javaType = TypeMappingEnum.getJavaType(column.getDataType());
            column.setJavaType(javaType);

            String jdbcType = TypeMappingEnum.getJdbcType(column.getDataType());
            column.setJdbcType(jdbcType);

            return column;
        }
    }

    public static String toCamelCase(String str) {
        return toCamelCase(str, false);
    }

    public static String toCamelCase(String str, boolean capitalizeFirst) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_' || c == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else if (i == 0 && capitalizeFirst) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}