package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
        context.put("pkType", tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getJavaType() : "number");
        context.put("pkName", tableInfo.getPrimaryKey() != null ? TableReader.toCamelCase(tableInfo.getPrimaryKey().getColumnName()) : "id");
        context.put("description", tableInfo.getTableComment());

        String apiName = tableInfo.getEntityName().replace("Entity", "") + "Api";
        context.put("apiName", apiName);

        String fileName = toFileName(tableInfo.getEntityName()) + "Api";
        context.put("fileName", fileName);

        String template = getApiTemplate();
        return templateEngine.renderString(template, context);
    }

    private String getApiTemplate() {
        return """
import request from '@/utils/request';
import type { ${table.entityName} } from './types';

#set($baseName = ${table.entityName})
#if($baseName.endsWith("Entity"))
#set($baseName = $baseName.substring(0, $baseName.length() - 6))
#end

export const ${table.entityLowerName}Api = {
  getList: (params?: any) =>
    request({
      url: '/${table.entityLowerName}',
      method: 'GET',
      params,
    }),

  getById: (${table.primaryKey.fieldName}: ${table.primaryKey.javaType}) =>
    request({
      url: '/${table.entityLowerName}/${table.primaryKey.fieldName}',
      method: 'GET',
    }),

  create: (data: ${table.entityName}) =>
    request({
      url: '/${table.entityLowerName}',
      method: 'POST',
      data,
    }),

  update: (data: ${table.entityName}) =>
    request({
      url: '/${table.entityLowerName}',
      method: 'PUT',
      data,
    }),

  delete: (${table.primaryKey.fieldName}: ${table.primaryKey.javaType}) =>
    request({
      url: '/${table.entityLowerName}/${table.primaryKey.fieldName}',
      method: 'DELETE',
    }),
};
""";
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