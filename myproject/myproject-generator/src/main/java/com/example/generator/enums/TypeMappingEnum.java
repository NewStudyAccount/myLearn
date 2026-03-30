package com.example.generator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum TypeMappingEnum {

    VARCHAR("varchar", "String", "VARCHAR"),
    CHAR("char", "String", "CHAR"),
    TEXT("text", "String", "CLOB"),
    TINYTEXT("tinytext", "String", "CLOB"),
    MEDIUMTEXT("mediumtext", "String", "CLOB"),
    LONGTEXT("longtext", "String", "CLOB"),

    TINYINT("tinyint", "Integer", "TINYINT"),
    SMALLINT("smallint", "Integer", "SMALLINT"),
    INT("int", "Integer", "INTEGER"),
    INTEGER("integer", "Integer", "INTEGER"),
    BIGINT("bigint", "Long", "BIGINT"),
    FLOAT("float", "Float", "REAL"),
    DOUBLE("double", "Double", "DOUBLE"),
    DECIMAL("decimal", "java.math.BigDecimal", "DECIMAL"),
    NUMERIC("numeric", "java.math.BigDecimal", "DECIMAL"),

    DATE("date", "java.time.LocalDate", "DATE"),
    DATETIME("datetime", "java.time.LocalDateTime", "TIMESTAMP"),
    TIMESTAMP("timestamp", "java.time.LocalDateTime", "TIMESTAMP"),
    TIME("time", "java.time.LocalTime", "TIME"),
    YEAR("year", "Integer", "INTEGER"),

    BIT("bit", "Boolean", "BIT"),
    BOOLEAN("boolean", "Boolean", "BOOLEAN"),

    BLOB("blob", "byte[]", "BLOB"),
    TINYBLOB("tinyblob", "byte[]", "BLOB"),
    MEDIUMBLOB("mediumblob", "byte[]", "BLOB"),
    LONGBLOB("longblob", "byte[]", "BLOB");

    private final String dbType;
    private final String javaType;
    private final String jdbcType;

    private static final Map<String, TypeMappingEnum> DB_TYPE_MAP = new HashMap<>();

    static {
        for (TypeMappingEnum type : values()) {
            DB_TYPE_MAP.put(type.getDbType().toLowerCase(), type);
        }
    }

    public static TypeMappingEnum getByDbType(String dbType) {
        if (dbType == null) {
            return VARCHAR;
        }
        String lowerDbType = dbType.toLowerCase();
        
        if (lowerDbType.contains("(")) {
            lowerDbType = lowerDbType.substring(0, lowerDbType.indexOf("("));
        }
        
        return DB_TYPE_MAP.getOrDefault(lowerDbType, VARCHAR);
    }

    public static String getJavaType(String dbType) {
        return getByDbType(dbType).getJavaType();
    }

    public static String getJdbcType(String dbType) {
        return getByDbType(dbType).getJdbcType();
    }
}