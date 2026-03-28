package com.example.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    private String outputPath = "target/generated-sources";

    private String basePackage = "com.example";

    private String author = "generator";

    private boolean overwrite = false;

    private String tablePrefix = "";

    private String entityPrefix = "";

    private String tableName = "";

    private boolean generateController = true;

    private boolean generateService = true;

    private boolean generateMapper = true;

    private boolean generateEntity = true;

    private boolean generateFrontend = true;

    private String frontendOutputPath = "target/generated-frontend";

    private String apiPath = "src/api";
}