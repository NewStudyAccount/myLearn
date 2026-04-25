package com.example.service.impl;

import com.example.domain.ColumnInfo;
import com.example.domain.TableInfo;
import com.example.service.GeneratorService;
import com.example.utils.DatabaseMetaService;
import com.example.utils.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private DatabaseMetaService databaseMetaService;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public List<TableInfo> getTables() {
        return databaseMetaService.getTables();
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        return databaseMetaService.getTableInfo(tableName);
    }

    @Override
    public Map<String, String> generateCode(String tableName, String tablePrefix, String packageName) {
        TableInfo tableInfo = databaseMetaService.getTableInfo(tableName);
        if (tableInfo == null) {
            throw new RuntimeException("Table not found: " + tableName);
        }

        String className = tableInfo.getClassName();
        if (tablePrefix != null && !tablePrefix.isEmpty() && tableName.startsWith(tablePrefix)) {
            String rawName = tableName.substring(tablePrefix.length());
            className = underlineToCamel(rawName, true);
        }

        String classNameLower = className.substring(0, 1).toLowerCase() + className.substring(1);

        String primaryKeyFieldName = "id";
        if (tableInfo.getColumns() != null) {
            for (ColumnInfo col : tableInfo.getColumns()) {
                if (col.isPrimaryKey()) {
                    primaryKeyFieldName = col.getFieldName();
                    break;
                }
            }
        }

        Map<String, Object> context = new HashMap<>();
        context.put("package", packageName);
        context.put("tableName", tableName);
        context.put("className", className);
        context.put("classNameLower", classNameLower);
        context.put("tableComment", tableInfo.getTableComment());
        context.put("columns", tableInfo.getColumns());
        context.put("primaryKeyFieldName", primaryKeyFieldName);

        Map<String, String> result = new LinkedHashMap<>();

        // Backend code
        result.put("backend/domain/" + className + ".java", templateEngine.render("templates/generator/domain.vm", context));
        result.put("backend/domain/req/" + classNameLower + "/" + className + "QueryPageReq.java", templateEngine.render("templates/generator/pageQuery.vm", context));
        result.put("backend/mapper/" + className + "Mapper.java", templateEngine.render("templates/generator/mapper.vm", context));
        result.put("backend/service/" + className + "Service.java", templateEngine.render("templates/generator/service.vm", context));
        result.put("backend/service/impl/" + className + "ServiceImpl.java", templateEngine.render("templates/generator/serviceImpl.vm", context));
        result.put("backend/controller/" + className + "Controller.java", templateEngine.render("templates/generator/controller.vm", context));

        // Frontend code
        result.put("frontend/views/" + classNameLower + "/index.vue", templateEngine.render("templates/generator/list.vue.vm", context));
        result.put("frontend/api/" + classNameLower + "Api.ts", templateEngine.render("templates/generator/api.ts.vm", context));
        result.put("frontend/router/" + classNameLower + ".ts", templateEngine.render("templates/generator/router.ts.vm", context));

        return result;
    }
    @Override
    public byte[] downloadCode(String tableName, String tablePrefix, String packageName) {
        Map<String, String> codeMap = generateCode(tableName, tablePrefix, packageName);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Map.Entry<String, String> entry : codeMap.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                zos.write(entry.getValue().getBytes("UTF-8"));
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create zip file", e);
        }
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
