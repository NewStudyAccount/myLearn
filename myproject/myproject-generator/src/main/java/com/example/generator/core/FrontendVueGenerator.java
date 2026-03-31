package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.ColumnInfo;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FrontendVueGenerator {

    private static final Logger log = LoggerFactory.getLogger(FrontendVueGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public FrontendVueGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

    public String generate(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        
        tableInfo.setLowerEntityName(tableInfo.getEntityName().substring(0, 1).toLowerCase() + tableInfo.getEntityName().substring(1));
        
        String pkName = tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getFieldName() : "id";
        String pkCapitalName = pkName.substring(0, 1).toUpperCase() + pkName.substring(1);
        
        context.put("table", tableInfo);
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityLowerName", tableInfo.getLowerEntityName());
        context.put("pkName", pkName);
        context.put("pkCapitalName", pkCapitalName);
        context.put("description", tableInfo.getTableComment());

        List<ColumnInfo> formColumns = tableInfo.getColumns().stream()
            .filter(c -> !c.isPrimaryKey())
            .collect(Collectors.toList());
        context.put("formColumns", formColumns);

        List<ColumnInfo> searchColumns = tableInfo.getColumns().stream()
            .filter(c -> !c.isPrimaryKey() && ("String".equals(c.getJavaType()) || "Integer".equals(c.getJavaType())))
            .collect(Collectors.toList());
        context.put("searchColumns", searchColumns);

        context.put("apiPath", properties.getApiPath().replace("\\", "/"));
        
        String entityName = tableInfo.getEntityName();
        if (entityName.endsWith("Entity")) {
            entityName = entityName.substring(0, entityName.length() - 6);
        }
        context.put("baseName", entityName);
        
        context.put("apiGetList", "getList" + entityName);
        context.put("apiGetById", "getById" + entityName);
        context.put("apiCreate", "create" + entityName);
        context.put("apiUpdate", "update" + entityName);
        context.put("apiDelete", "delete" + entityName);
        
        String apiFilePath = "@/api" + properties.getApiPath().replace("\\", "/") + "/" + tableInfo.getLowerEntityName() + "Api";
        context.put("apiFilePath", apiFilePath);

        return templateEngine.render("templates/frontend/vue.vm", context);
    }
}