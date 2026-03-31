package com.example.generator.core;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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
            String templateContent = loadTemplateContent(templatePath);
            return renderString(templateContent, contextMap);
        } catch (Exception e) {
            log.error("Failed to render template: {}, template path: {}", templatePath, templatePath, e);
            throw new RuntimeException("Failed to render template: " + templatePath, e);
        }
    }

    private String loadTemplateContent(String templatePath) throws IOException {
        String normalizedPath = templatePath.startsWith("/") ? templatePath : "/" + templatePath;
        
        if (templatePath.startsWith("file:")) {
            String filePath = templatePath.substring(5);
            Resource resource = new FileSystemResource(filePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
            throw new RuntimeException("Template file not found: " + filePath);
        }
        
        try {
            ClassPathResource resource = new ClassPathResource(normalizedPath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            log.debug("Template not found in classpath: {}, trying file system", normalizedPath, e);
        }
        
        File file = new File(templatePath);
        if (file.exists()) {
            try (InputStream is = new java.io.FileInputStream(file)) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
        
        throw new RuntimeException("Template not found: " + templatePath + 
            ". Searched in classpath as: " + normalizedPath + 
            ", and as file system path: " + file.getAbsolutePath());
    }

    public String renderString(String templateContent, Map<String, Object> contextMap) {
        try {
            VelocityContext context = new VelocityContext(contextMap);
            StringWriter writer = new StringWriter();
            velocityEngine.evaluate(context, writer, "inline-template", templateContent);
            return writer.toString();
        } catch (Exception e) {
            log.error("Failed to render template, template length: {}", templateContent.length(), e);
            throw new RuntimeException("Failed to render template: " + e.getMessage(), e);
        }
    }
}