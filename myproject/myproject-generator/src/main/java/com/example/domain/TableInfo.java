package com.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {

    private String tableName;

    private String tableComment;

    private List<ColumnInfo> columns;

    public String getClassName() {
        return underlineToCamel(tableName, true);
    }

    public String getClassNameLower() {
        return underlineToCamel(tableName, false);
    }

    private String underlineToCamel(String str, boolean firstUpperCase) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = firstUpperCase;
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
