package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableInfo;
import com.example.domain.req.GenerateReq;
import com.example.service.GeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "代码生成器")
@RestController
@RequestMapping("/project/generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @Operation(summary = "获取数据库表列表")
    @GetMapping("/tables")
    public Response<?> getTables() {
        List<TableInfo> tables = generatorService.getTables();
        return Response.success(tables);
    }

    @Operation(summary = "获取表结构信息")
    @GetMapping("/table/{tableName}")
    public Response<TableInfo> getTableInfo(@PathVariable String tableName) {
        TableInfo tableInfo = generatorService.getTableInfo(tableName);
        return Response.success(tableInfo);
    }

    @Operation(summary = "生成代码预览")
    @PostMapping("/generate")
    public Response<Map<String, String>> generateCode(@RequestBody GenerateReq generateReq) {
        String tableName = generateReq.getTableName();
        String tablePrefix = generateReq.getTablePrefix();
        String packageName = StringUtils.isEmpty(generateReq.getPackageName())? "com.example":generateReq.getPackageName();
        Map<String, String> codeMap = generatorService.generateCode(tableName, tablePrefix, packageName);
        return Response.success(codeMap);
    }

    @Operation(summary = "下载生成的代码")
    @PostMapping("/download")
    public void downloadCode(@RequestBody GenerateReq generateReq, jakarta.servlet.http.HttpServletResponse response) {
        try {
            String tableName = generateReq.getTableName();
            String tablePrefix = generateReq.getTablePrefix();
            String packageName = StringUtils.isEmpty(generateReq.getPackageName())? "com.example":generateReq.getPackageName();

            byte[] zipBytes = generatorService.downloadCode(tableName, tablePrefix, packageName);

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + tableName + "_code.zip");
            response.getOutputStream().write(zipBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("Download failed", e);
        }
    }
}
