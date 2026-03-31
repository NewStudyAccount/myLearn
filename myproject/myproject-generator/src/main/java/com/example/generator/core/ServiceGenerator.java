package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceGenerator {

    private static final Logger log = LoggerFactory.getLogger(ServiceGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public ServiceGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

    public String generateInterface(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("packageName", properties.getBasePackage());
        context.put("domainPackage", properties.getBasePackage() + ".domain");

        String servicePackage = properties.getBasePackage() + ".service";
        String mapperPackage = properties.getBasePackage() + ".mapper";

        context.put("servicePackage", servicePackage);
        context.put("mapperPackage", mapperPackage);
        context.put("serviceName", tableInfo.getEntityName() + "Service");
        context.put("mapperName", tableInfo.getEntityName() + "Mapper");
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityLowerName", tableInfo.getLowerEntityName());

        return templateEngine.render("templates/backend/service.vm", context);
    }

    public String generateImpl(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        context.put("table", tableInfo);
        context.put("packageName", properties.getBasePackage());
        context.put("domainPackage", properties.getBasePackage() + ".domain");

        String serviceImplPackage = properties.getBasePackage() + ".service.impl";
        String servicePackage = properties.getBasePackage() + ".service";
        String mapperPackage = properties.getBasePackage() + ".mapper";

        context.put("serviceImplPackage", serviceImplPackage);
        context.put("servicePackage", servicePackage);
        context.put("mapperPackage", mapperPackage);
        context.put("serviceName", tableInfo.getEntityName() + "Service");
        context.put("implName", tableInfo.getEntityName() + "ServiceImpl");
        context.put("mapperName", tableInfo.getEntityName() + "Mapper");
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityLowerName", tableInfo.getLowerEntityName());
        context.put("pkType", tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getJavaType() : "Long");
        context.put("pkName", tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getFieldName() : "id");

        return templateEngine.render("templates/backend/service-impl.vm", context);
    }
}