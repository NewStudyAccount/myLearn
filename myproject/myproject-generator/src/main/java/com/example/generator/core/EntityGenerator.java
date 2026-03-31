package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.ColumnInfo;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityGenerator {

    private static final Logger log = LoggerFactory.getLogger(EntityGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public EntityGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

    public String generate(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("properties", properties);
        context.put("packageName", properties.getBasePackage());
        context.put("author", properties.getAuthor());
        context.put("date", new java.util.Date());

        String entityName = tableInfo.getTableName();
        if (properties.getTablePrefix() != null && !properties.getTablePrefix().isEmpty()) {
            entityName = entityName.replaceFirst(properties.getTablePrefix(), "");
        }
        entityName = TableReader.toCamelCase(entityName, true);
        if (properties.getEntityPrefix() != null && !properties.getEntityPrefix().isEmpty()) {
            entityName = properties.getEntityPrefix() + entityName;
        }

        tableInfo.setEntityName(entityName);
        tableInfo.setLowerEntityName(entityName.substring(0, 1).toLowerCase() + entityName.substring(1));

        return templateEngine.render("templates/backend/entity.vm", context);
    }
}