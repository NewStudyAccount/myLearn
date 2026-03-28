package com.example.generator.controller;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.core.ControllerGenerator;
import com.example.generator.core.EntityGenerator;
import com.example.generator.core.FrontendApiGenerator;
import com.example.generator.core.FrontendVueGenerator;
import com.example.generator.core.MapperGenerator;
import com.example.generator.core.ServiceGenerator;
import com.example.generator.core.TableReader;
import com.example.generator.domain.TableInfo;
import com.example.generator.utils.CodeZipUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/generator")
@Tag(name = "代码生成器")
public class GeneratorController {

    private static final Logger log = LoggerFactory.getLogger(GeneratorController.class);

    private final TableReader tableReader;
    private final EntityGenerator entityGenerator;
    private final MapperGenerator mapperGenerator;
    private final ServiceGenerator serviceGenerator;
    private final ControllerGenerator controllerGenerator;
    private final FrontendApiGenerator frontendApiGenerator;
    private final FrontendVueGenerator frontendVueGenerator;
    private final GeneratorProperties properties;

    public GeneratorController(
            TableReader tableReader,
            EntityGenerator entityGenerator,
            MapperGenerator mapperGenerator,
            ServiceGenerator serviceGenerator,
            ControllerGenerator controllerGenerator,
            FrontendApiGenerator frontendApiGenerator,
            FrontendVueGenerator frontendVueGenerator,
            GeneratorProperties properties) {
        this.tableReader = tableReader;
        this.entityGenerator = entityGenerator;
        this.mapperGenerator = mapperGenerator;
        this.serviceGenerator = serviceGenerator;
        this.controllerGenerator = controllerGenerator;
        this.frontendApiGenerator = frontendApiGenerator;
        this.frontendVueGenerator = frontendVueGenerator;
        this.properties = properties;
    }

    @Operation(summary = "获取所有表")
    @GetMapping("/tables")
    public List<String> getTables() {
        return tableReader.getAllTables();
    }

    @Operation(summary = "获取表结构")
    @GetMapping("/table/{tableName}")
    public TableInfo getTableInfo(@PathVariable("tableName") String tableName) {
        return tableReader.readTable(tableName);
    }

    @Operation(summary = "生成代码")
    @PostMapping("/generate")
    public Map<String, String> generate(@RequestBody GenerateRequest request) {
        properties.setTableName(request.getTableName());

        TableInfo tableInfo = tableReader.readTable(request.getTableName());

        if (request.getTablePrefix() != null) {
            properties.setTablePrefix(request.getTablePrefix());
        }
        if (request.getEntityPrefix() != null) {
            properties.setEntityPrefix(request.getEntityPrefix());
        }

        Map<String, String> result = new HashMap<>();

        if (request.isGenerateEntity() || request.getGenerateEntity() == null) {
            result.put("entity", entityGenerator.generate(tableInfo));
        }

        if (request.isGenerateMapper() || request.getGenerateMapper() == null) {
            result.put("mapperInterface", mapperGenerator.generateInterface(tableInfo));
            result.put("mapperXml", mapperGenerator.generateXml(tableInfo));
        }

        if (request.isGenerateService() || request.getGenerateService() == null) {
            result.put("serviceInterface", serviceGenerator.generateInterface(tableInfo));
            result.put("serviceImpl", serviceGenerator.generateImpl(tableInfo));
        }

        if (request.isGenerateController() || request.getGenerateController() == null) {
            result.put("controller", controllerGenerator.generate(tableInfo));
        }

        if (request.isGenerateFrontend() || request.getGenerateFrontend() == null) {
            result.put("frontendApi", frontendApiGenerator.generate(tableInfo));
            result.put("frontendVue", frontendVueGenerator.generate(tableInfo));
        }

        return result;
    }

    @Operation(summary = "下载生成代码")
    @PostMapping("/download")
    public void download(@RequestBody GenerateRequest request, HttpServletResponse response) {
        try {
            Map<String, String> generatedCode = generate(request);
            
            String entityName = TableReader.toCamelCase(request.getTableName());
            if (request.getTablePrefix() != null && request.getTableName().startsWith(request.getTablePrefix())) {
                entityName = TableReader.toCamelCase(request.getTableName().substring(request.getTablePrefix().length()));
            }
            if (request.getEntityPrefix() != null) {
                entityName = request.getEntityPrefix() + entityName;
            }

            Map<String, String> zipEntries = new HashMap<>();
            String packagePath = properties.getBasePackage().replace(".", "/");

            if (generatedCode.containsKey("entity")) {
                zipEntries.put(packagePath + "/domain/" + entityName + ".java", generatedCode.get("entity"));
            }
            if (generatedCode.containsKey("mapperInterface")) {
                zipEntries.put(packagePath + "/mapper/" + entityName + "Mapper.java", generatedCode.get("mapperInterface"));
            }
            if (generatedCode.containsKey("mapperXml")) {
                zipEntries.put("mapper/" + entityName + "Mapper.xml", generatedCode.get("mapperXml"));
            }
            if (generatedCode.containsKey("serviceInterface")) {
                zipEntries.put(packagePath + "/service/I" + entityName + "Service.java", generatedCode.get("serviceInterface"));
            }
            if (generatedCode.containsKey("serviceImpl")) {
                zipEntries.put(packagePath + "/service/impl/" + entityName + "ServiceImpl.java", generatedCode.get("serviceImpl"));
            }
            if (generatedCode.containsKey("controller")) {
                zipEntries.put(packagePath + "/controller/" + entityName + "Controller.java", generatedCode.get("controller"));
            }
            if (generatedCode.containsKey("frontendApi")) {
                zipEntries.put("src/api/" + entityName.toLowerCase() + "Api.ts", generatedCode.get("frontendApi"));
            }
            if (generatedCode.containsKey("frontendVue")) {
                zipEntries.put("src/views/" + entityName.toLowerCase() + ".vue", generatedCode.get("frontendVue"));
            }

            String fileName = entityName + "_code.zip";
            response.setContentType("application/zip");
            response.setCharacterEncoding("UTF-8");
            
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

            CodeZipUtil.zipFiles(zipEntries, response.getOutputStream());
            
        } catch (IOException e) {
            log.error("Error generating download", e);
        }
    }

    public static class GenerateRequest {
        private String tableName;
        private String tablePrefix;
        private String entityPrefix;
        private boolean generateEntity = true;
        private boolean generateMapper = true;
        private boolean generateService = true;
        private boolean generateController = true;
        private boolean generateFrontend = true;

        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }
        public String getTablePrefix() { return tablePrefix; }
        public void setTablePrefix(String tablePrefix) { this.tablePrefix = tablePrefix; }
        public String getEntityPrefix() { return entityPrefix; }
        public void setEntityPrefix(String entityPrefix) { this.entityPrefix = entityPrefix; }
        public Boolean getGenerateEntity() { return generateEntity; }
        public void setGenerateEntity(Boolean generateEntity) { this.generateEntity = generateEntity; }
        public Boolean getGenerateMapper() { return generateMapper; }
        public void setGenerateMapper(Boolean generateMapper) { this.generateMapper = generateMapper; }
        public Boolean getGenerateService() { return generateService; }
        public void setGenerateService(Boolean generateService) { this.generateService = generateService; }
        public Boolean getGenerateController() { return generateController; }
        public void setGenerateController(Boolean generateController) { this.generateController = generateController; }
        public Boolean getGenerateFrontend() { return generateFrontend; }
        public void setGenerateFrontend(Boolean generateFrontend) { this.generateFrontend = generateFrontend; }

        public boolean isGenerateEntity() { return generateEntity; }
        public boolean isGenerateMapper() { return generateMapper; }
        public boolean isGenerateService() { return generateService; }
        public boolean isGenerateController() { return generateController; }
        public boolean isGenerateFrontend() { return generateFrontend; }
    }
}