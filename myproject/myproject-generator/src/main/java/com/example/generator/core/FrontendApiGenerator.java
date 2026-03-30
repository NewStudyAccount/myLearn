package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.ColumnInfo;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FrontendApiGenerator {

    private static final Logger log = LoggerFactory.getLogger(FrontendApiGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public FrontendApiGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

    public String generate(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityLowerName", tableInfo.getLowerEntityName());
        context.put("pkType", tableInfo.getPrimaryKey() != null ? getTsType(tableInfo.getPrimaryKey().getJavaType()) : "number");
        context.put("pkName", tableInfo.getPrimaryKey() != null ? TableReader.toCamelCase(tableInfo.getPrimaryKey().getColumnName()) : "id");
        context.put("description", tableInfo.getTableComment());

        String apiName = tableInfo.getEntityName().replace("Entity", "") + "Api";
        context.put("apiName", apiName);

        String fileName = tableInfo.getLowerEntityName() + "Api";
        context.put("fileName", fileName);

        String resourceName = extractResourceName(tableInfo.getTableName());
        context.put("resourceName", resourceName);
        context.put("resourceNameLower", resourceName.substring(0, 1).toLowerCase() + resourceName.substring(1));

        List<ColumnInfo> columns = new ArrayList<>();
        for (ColumnInfo column : tableInfo.getColumns()) {
            ColumnInfo col = new ColumnInfo(column);
            col.setTsType(getTsType(column.getJavaType()));
            columns.add(col);
        }
        context.put("columns", columns);

        if (tableInfo.getPrimaryKey() != null) {
            context.put("pkTsType", getTsType(tableInfo.getPrimaryKey().getJavaType()));
        }

        String template = getApiTemplate();
        return templateEngine.renderString(template, context);
    }
    private String getApiTemplate() {
        return """
import http from '@/utils/http';
import type {AxiosPromise} from "axios";



#set($baseName = ${table.entityName})
#if($baseName.endsWith("Entity"))
#set($baseName = $baseName.substring(0, $baseName.length() - 6))
#end


export interface ${entityName} {
#foreach($column in ${columns})
  /** ${column.columnComment} */
  #if($column.nullable)$column.fieldName?: $column.tsType#else$column.fieldName: $column.tsType#end

#end
}

export function getList${baseName}(query?: any): AxiosPromise<any> {
  return http({
    url: '/system/${resourceName}/list',
    method: 'get',
    params: query
  });
}

export function getById${baseName}(${pkName}: ${pkTsType}): AxiosPromise<${entityName}> {
  return http({
    url: `/system/${resourceName}/${${pkName}}`,
    method: 'get'
  });
}

export function create${baseName}(data: ${entityName}): AxiosPromise<void> {
  return http({
    url: '/system/${resourceName}',
    method: 'post',
    data
  });
}

export function update${baseName}(data: ${entityName}): AxiosPromise<void> {
  return http({
    url: '/system/${resourceName}',
    method: 'put',
    data
  });
}

export function delete${baseName}(${pkName}: ${pkTsType}): AxiosPromise<void> {
  return http({
    url: `/system/${resourceName}/${${pkName}}`,
    method: 'delete'
  });
}
""";
    }


    private String getTsType(String javaType) {
        if (javaType == null) {
            return "any";
        }
        return switch (javaType) {
            case "Long", "long" -> "string";  // Long 类型转为 string，避免精度丢失
            case "Integer", "int", "Short", "short", "Byte", "byte" -> "number";
            case "Double", "double", "Float", "float" -> "number";
            case "BigDecimal" -> "string";  // BigDecimal 也建议转为 string
            case "Boolean", "boolean" -> "boolean";
            case "String", "Character", "char" -> "string";
            case "Date", "LocalDateTime", "LocalDate", "LocalTime" -> "string";
            default -> "any";
        };
    }

    private String extractResourceName(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return "";
        }

        String nameWithoutPrefix = tableName;
        if (tableName.toLowerCase().startsWith("sys_")) {
            nameWithoutPrefix = tableName.substring(4);
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < nameWithoutPrefix.length(); i++) {
            char c = nameWithoutPrefix.charAt(i);
            if (c == '_' || c == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else if (i == 0) {
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private String toFileName(String entityName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entityName.length(); i++) {
            char c = entityName.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append("-");
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}