package com.example.domain;

import lombok.Data;

@Data
public class ColumnInfo {

    private String columnName;

    private String dataType;

    private String columnComment;

    private boolean nullable;

    private String columnDefault;

    private String columnKey;

    private String extra;

    private Integer maxLength;

    public String getFieldName() {
        return underlineToCamel(columnName);
    }

    public String getJavaType() {
        return TypeMappingConfig.getJavaType(dataType);
    }

    public String getTsType() {
        return TypeMappingConfig.getTsType(dataType);
    }

    public boolean isPrimaryKey() {
        return "PRI".equals(columnKey);
    }

    public boolean isAutoIncrement() {
        return "auto_increment".equals(extra);
    }

    private String underlineToCamel(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else {
                sb.append(nextUpperCase ? Character.toUpperCase(c) : c);
                nextUpperCase = false;
            }
        }
        return sb.toString();
    }
}
