package com.example.generator.core;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@Component
public class TemplateEngine {

    private static final Logger log = LoggerFactory.getLogger(TemplateEngine.class);

    private final VelocityEngine velocityEngine;

    public TemplateEngine() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty("parser.pool.size", "20");
        properties.setProperty("directive.foreach.skip.empty", "true");

        this.velocityEngine = new VelocityEngine(properties);
        this.velocityEngine.init();
    }

    public String render(String templatePath, Map<String, Object> contextMap) {
        try {
            VelocityContext context = new VelocityContext(contextMap);
            StringWriter writer = new StringWriter();
            velocityEngine.getTemplate(templatePath, "UTF-8").merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("Failed to render template: {}", templatePath, e);
            throw new RuntimeException("Failed to render template: " + templatePath, e);
        }
    }

    public String renderString(String templateContent, Map<String, Object> contextMap) {
        try {
            VelocityContext context = new VelocityContext(contextMap);
            StringWriter writer = new StringWriter();
            velocityEngine.evaluate(context, writer, "inline-template", templateContent);
            return writer.toString();
        } catch (Exception e) {
            log.error("Failed to render inline template", e);
            throw new RuntimeException("Failed to render inline template", e);
        }
    }
}