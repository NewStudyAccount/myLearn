package com.example.generator.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String columnName;

    private String columnComment;

    private String columnType;

    private String dataType;

    private String fieldName;

    private String fieldNameCapitalized;

    private String javaType;

    private String jdbcType;

    private Boolean nullable = true;

    private Boolean primaryKey = false;

    private Boolean autoIncrement = false;

    private String defaultValue;

    private Integer columnSize;

    private Integer decimalDigits;

    private String comment;

    public String getColumnComment() {
        return columnComment == null ? "" : columnComment;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldNameCapitalized() {
        return fieldNameCapitalized;
    }

    public boolean isNullable() {
        return nullable != null && nullable;
    }

    public boolean isPrimaryKey() {
        return primaryKey != null && primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement != null && autoIncrement;
    }
}