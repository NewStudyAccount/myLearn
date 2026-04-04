package com.example.utils;

import com.example.domain.ColumnInfo;
import com.example.domain.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseMetaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TableInfo> getTables() {
        String sql = """
                SELECT TABLE_NAME, TABLE_COMMENT
                FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_TYPE = 'BASE TABLE'
                ORDER BY TABLE_NAME
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TableInfo table = new TableInfo();
            table.setTableName(rs.getString("TABLE_NAME"));
            table.setTableComment(rs.getString("TABLE_COMMENT"));
            return table;
        });
    }

    public TableInfo getTableInfo(String tableName) {
        String sql = """
                SELECT TABLE_NAME, TABLE_COMMENT
                FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_NAME = ?
                """;
        List<TableInfo> tables = jdbcTemplate.query(sql, ps -> ps.setString(1, tableName), (rs, rowNum) -> {
            TableInfo table = new TableInfo();
            table.setTableName(rs.getString("TABLE_NAME"));
            table.setTableComment(rs.getString("TABLE_COMMENT"));
            return table;
        });
        if (tables.isEmpty()) {
            return null;
        }
        TableInfo table = tables.get(0);
        table.setColumns(getColumns(tableName));
        return table;
    }

    public List<ColumnInfo> getColumns(String tableName) {
        String sql = """
                SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, IS_NULLABLE, COLUMN_DEFAULT,
                       COLUMN_KEY, EXTRA, CHARACTER_MAXIMUM_LENGTH
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_NAME = ?
                ORDER BY ORDINAL_POSITION
                """;
        return jdbcTemplate.query(sql, ps -> ps.setString(1, tableName), (rs, rowNum) -> {
            ColumnInfo column = new ColumnInfo();
            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setDataType(rs.getString("DATA_TYPE"));
            column.setColumnComment(rs.getString("COLUMN_COMMENT"));
            column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
            column.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
            column.setColumnKey(rs.getString("COLUMN_KEY"));
            column.setExtra(rs.getString("EXTRA"));
            column.setMaxLength(rs.getObject("CHARACTER_MAXIMUM_LENGTH") != null
                    ? rs.getInt("CHARACTER_MAXIMUM_LENGTH") : null);
            return column;
        });
    }
}
