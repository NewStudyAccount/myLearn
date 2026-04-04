package com.example.service;

import com.example.domain.TableInfo;

import java.util.List;
import java.util.Map;

public interface GeneratorService {

    List<TableInfo> getTables();

    TableInfo getTableInfo(String tableName);

    Map<String, String> generateCode(String tableName, String tablePrefix, String packageName);

    byte[] downloadCode(String tableName, String tablePrefix, String packageName);
}
