package com.example.domain;

import java.util.HashMap;
import java.util.Map;

public class TypeMappingConfig {

    private static final Map<String, String> JAVA_TYPE_MAP = new HashMap<>();
    private static final Map<String, String> TS_TYPE_MAP = new HashMap<>();

    static {
        // String types
        JAVA_TYPE_MAP.put("varchar", "String");
        JAVA_TYPE_MAP.put("char", "String");
        JAVA_TYPE_MAP.put("text", "String");
        JAVA_TYPE_MAP.put("longtext", "String");
        JAVA_TYPE_MAP.put("mediumtext", "String");
        JAVA_TYPE_MAP.put("tinytext", "String");
        JAVA_TYPE_MAP.put("enum", "String");
        JAVA_TYPE_MAP.put("set", "String");

        // Integer types
        JAVA_TYPE_MAP.put("tinyint", "Integer");
        JAVA_TYPE_MAP.put("smallint", "Integer");
        JAVA_TYPE_MAP.put("mediumint", "Integer");
        JAVA_TYPE_MAP.put("int", "Integer");
        JAVA_TYPE_MAP.put("integer", "Integer");

        // Long types
        JAVA_TYPE_MAP.put("bigint", "Long");

        // Float types
        JAVA_TYPE_MAP.put("float", "Float");
        JAVA_TYPE_MAP.put("double", "Double");

        // Decimal types
        JAVA_TYPE_MAP.put("decimal", "java.math.BigDecimal");
        JAVA_TYPE_MAP.put("numeric", "java.math.BigDecimal");

        // Date types
        JAVA_TYPE_MAP.put("date", "java.time.LocalDate");
        JAVA_TYPE_MAP.put("datetime", "java.time.LocalDateTime");
        JAVA_TYPE_MAP.put("timestamp", "java.time.LocalDateTime");
        JAVA_TYPE_MAP.put("time", "java.time.LocalTime");

        // Boolean
        JAVA_TYPE_MAP.put("boolean", "Boolean");
        JAVA_TYPE_MAP.put("bit", "Boolean");

        // Binary types
        JAVA_TYPE_MAP.put("blob", "byte[]");
        JAVA_TYPE_MAP.put("longblob", "byte[]");
        JAVA_TYPE_MAP.put("mediumblob", "byte[]");
        JAVA_TYPE_MAP.put("tinyblob", "byte[]");
        JAVA_TYPE_MAP.put("binary", "byte[]");
        JAVA_TYPE_MAP.put("varbinary", "byte[]");

        // TypeScript type mapping
        TS_TYPE_MAP.put("varchar", "string");
        TS_TYPE_MAP.put("char", "string");
        TS_TYPE_MAP.put("text", "string");
        TS_TYPE_MAP.put("longtext", "string");
        TS_TYPE_MAP.put("mediumtext", "string");
        TS_TYPE_MAP.put("tinytext", "string");
        TS_TYPE_MAP.put("enum", "string");
        TS_TYPE_MAP.put("set", "string");

        TS_TYPE_MAP.put("tinyint", "number");
        TS_TYPE_MAP.put("smallint", "number");
        TS_TYPE_MAP.put("mediumint", "number");
        TS_TYPE_MAP.put("int", "number");
        TS_TYPE_MAP.put("integer", "number");
        TS_TYPE_MAP.put("bigint", "number");
        TS_TYPE_MAP.put("float", "number");
        TS_TYPE_MAP.put("double", "number");
        TS_TYPE_MAP.put("decimal", "number");
        TS_TYPE_MAP.put("numeric", "number");

        TS_TYPE_MAP.put("date", "string");
        TS_TYPE_MAP.put("datetime", "string");
        TS_TYPE_MAP.put("timestamp", "string");
        TS_TYPE_MAP.put("time", "string");

        TS_TYPE_MAP.put("boolean", "boolean");
        TS_TYPE_MAP.put("bit", "boolean");

        TS_TYPE_MAP.put("blob", "string");
        TS_TYPE_MAP.put("longblob", "string");
        TS_TYPE_MAP.put("mediumblob", "string");
        TS_TYPE_MAP.put("tinyblob", "string");
        TS_TYPE_MAP.put("binary", "string");
        TS_TYPE_MAP.put("varbinary", "string");
    }

    public static String getJavaType(String dbType) {
        return JAVA_TYPE_MAP.getOrDefault(dbType.toLowerCase(), "Object");
    }

    public static String getTsType(String dbType) {
        return TS_TYPE_MAP.getOrDefault(dbType.toLowerCase(), "any");
    }
}
