package com.example.generator.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tableName;

    private String tableComment;

    private String entityName;

    private String lowerEntityName;

    private String packageName;

    private List<ColumnInfo> columns = new ArrayList<>();

    private ColumnInfo primaryKey;

    private String author;

    private String date;

    public String getTableComment() {
        return tableComment == null ? "" : tableComment;
    }
}