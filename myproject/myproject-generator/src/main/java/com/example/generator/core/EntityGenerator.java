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
        entityName = TableReader.toCamelCase(entityName);
        if (properties.getEntityPrefix() != null && !properties.getEntityPrefix().isEmpty()) {
            entityName = properties.getEntityPrefix() + entityName;
        }

        tableInfo.setEntityName(entityName);
        tableInfo.setLowerEntityName(entityName.substring(0, 1).toLowerCase() + entityName.substring(1));

        String template = getEntityTemplate();
        return templateEngine.renderString(template, context);
    }

    private String getEntityTemplate() {
        return "package ${packageName}.domain;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport lombok.Data;\n\nimport java.io.Serializable;\n#foreach($column in $table.columns)\nimport ${column.javaType};\n#end\n\n/**\n * ${table.tableComment}\n * @TableName ${table.tableName}\n */\n@Data\n@TableName(value =\"${table.tableName}\")\npublic class ${table.entityName} implements Serializable {\n#foreach($column in $table.columns)\n    /**\n     * ${column.columnComment}\n     */\n#if($column.primaryKey)\n    @TableId(type = IdType.AUTO, value = \"${column.columnName}\")\n#end\n    @TableField(\"${column.columnName}\")\n    private ${column.javaType} ${column.fieldName};\n\n#end\n    @TableField(exist = false)\n    private static final long serialVersionUID = 1L;\n}\n";
    }
}