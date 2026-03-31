package com.example.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class TemplateEngine {

    private final VelocityEngine velocityEngine;

    public TemplateEngine() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        velocityEngine.setProperty("input.encoding", "UTF-8");
        velocityEngine.setProperty("output.encoding", "UTF-8");
        velocityEngine.init();
    }

    public String render(String templatePath, Map<String, Object> context) {
        VelocityContext velocityContext = new VelocityContext();
        context.forEach(velocityContext::put);

        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templatePath, "UTF-8", velocityContext, writer);
        return writer.toString();
    }
}
