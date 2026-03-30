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

    private String tsType;

    public ColumnInfo() {
    }

    public ColumnInfo(ColumnInfo source) {
        this.columnName = source.columnName;
        this.columnComment = source.columnComment;
        this.columnType = source.columnType;
        this.dataType = source.dataType;
        this.fieldName = source.fieldName;
        this.fieldNameCapitalized = source.fieldNameCapitalized;
        this.javaType = source.javaType;
        this.jdbcType = source.jdbcType;
        this.nullable = source.nullable;
        this.primaryKey = source.primaryKey;
        this.autoIncrement = source.autoIncrement;
        this.defaultValue = source.defaultValue;
        this.columnSize = source.columnSize;
        this.decimalDigits = source.decimalDigits;
        this.comment = source.comment;
    }

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