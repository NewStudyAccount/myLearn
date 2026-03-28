package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ControllerGenerator {

    private static final Logger log = LoggerFactory.getLogger(ControllerGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public ControllerGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

    public String generate(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("packageName", properties.getBasePackage());
        context.put("domainPackage", properties.getBasePackage() + ".domain");
        context.put("servicePackage", properties.getBasePackage() + ".service");

        String controllerPackage = properties.getBasePackage() + ".controller";
        String servicePackage = properties.getBasePackage() + ".service";

        context.put("controllerPackage", controllerPackage);
        context.put("servicePackage", servicePackage);
        context.put("controllerName", tableInfo.getEntityName() + "Controller");
        context.put("serviceName", tableInfo.getEntityName() + "Service");
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityLowerName", tableInfo.getLowerEntityName());
        context.put("pkType", tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getJavaType() : "Long");
        context.put("pkName", tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getFieldName() : "id");

        String mappingName = toMappingName(tableInfo.getEntityName());
        context.put("mappingName", mappingName);
        context.put("description", tableInfo.getTableComment());

        String template = getControllerTemplate();
        return templateEngine.renderString(template, context);
    }

    private String getControllerTemplate() {
        return """
package ${controllerPackage};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${domainPackage}.${entityName};
import ${servicePackage}.${serviceName};
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${table.tableComment}
 */
@Tag(name = "${description}")
@RestController
@RequestMapping("/${mappingName}")
public class ${controllerName} {

    private final ${serviceName} ${entityLowerName}Service;

    public ${controllerName}(${serviceName} ${entityLowerName}Service) {
        this.${entityLowerName}Service = ${entityLowerName}Service;
    }

    @Operation(summary = "列表")
    @GetMapping
    public Page<${entityName}> list(Page<${entityName}> page) {
        return ${entityLowerName}Service.page(page);
    }

    @Operation(summary = "详情")
    @GetMapping("/{${pkName}}")
    public ${entityName} getById(@PathVariable ${pkType} ${pkName}) {
        return ${entityLowerName}Service.getById(${pkName});
    }

    @Operation(summary = "新增")
    @PostMapping
    public boolean save(@RequestBody ${entityName} ${entityLowerName}) {
        return ${entityLowerName}Service.save(${entityLowerName});
    }

    @Operation(summary = "修改")
    @PutMapping
    public boolean update(@RequestBody ${entityName} ${entityLowerName}) {
        return ${entityLowerName}Service.updateById(${entityLowerName});
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{${pkName}}")
    public boolean delete(@PathVariable ${pkType} ${pkName}) {
        return ${entityLowerName}Service.removeById(${pkName});
    }
}
""";
    }

    private String toMappingName(String entityName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entityName.length(); i++) {
            char c = entityName.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append("/");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}