package com.example.oss.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import com.example.oss.factory.OssClientFactoryProvider;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssFileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * OSS文件操作控制器
 */
@Slf4j
@RestController
@RequestMapping("/project/sysOssFile")
@RequiredArgsConstructor
public class OssController {

    private final OssConfigService ossConfigService;

    private final OssClientFactoryProvider ossClientFactoryProvider;


    @Autowired
    private OssFileService ossFileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = ossFileService.uploadFile(file);
            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("size", file.getSize());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable("fileName") String fileName) {
        try {
            List<SysOssConfig> sysOssConfigs = ossConfigService.listActive();
            if (CollectionUtils.isEmpty(sysOssConfigs)) {
                return ResponseEntity.badRequest().body("未找到有效的OSS配置");
            }
            SysOssConfig sysOssConfig = sysOssConfigs.getFirst();
            byte[] data = ossClientFactoryProvider.getFactory(sysOssConfig).downloadFile(sysOssConfig, fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("文件下载失败: " + e.getMessage());
        }
    }

    private SysOssConfig getActiveConfig(String configName) {
        SysOssConfig sysOssConfig = ossConfigService.getByConfigName(configName);
        if (sysOssConfig == null) {
            throw new RuntimeException("OSS配置不存在: " + configName);
        }
        if (!Boolean.TRUE.equals(sysOssConfig.getIsActive())) {
            throw new RuntimeException("OSS配置未启用: " + configName);
        }
        return sysOssConfig;
    }



    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysOssFile>> list(@RequestBody SysOssFileQueryPageReq pageReq) {
        TableDataInfo<SysOssFile> tableDataInfo = ossFileService.querySysOssFileListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysOssFile> getById(@PathVariable Long id) {
        SysOssFile entity = ossFileService.queryById(id);
        return Response.success(entity);
    }



    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<?> delete(@PathVariable("id") Long id) {
        int i = ossFileService.deleteById(id);
        return Response.success(i);
    }

    // ==================== 导出端点 ====================

    @Operation(summary = "单文件导出")
    @GetMapping("/export/{id}")
    public ResponseEntity<?> exportFile(@PathVariable("id") Long id) {
        try {
            SysOssFile sysOssFile = ossFileService.queryById(id);
            if (sysOssFile == null) {
                return ResponseEntity.badRequest().body("文件不存在");
            }
            byte[] data = ossFileService.exportFile(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sysOssFile.getOriginalName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (Exception e) {
            log.error("文件导出失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("文件导出失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量文件导出为ZIP")
    @PostMapping("/export/batch")
    public ResponseEntity<?> exportBatch(@RequestBody List<Long> ossIds) {
        try {
            Map<String, byte[]> files = ossFileService.exportFiles(ossIds);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
                for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                    if (entry.getValue() != null) {
                        zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                        zipOut.write(entry.getValue());
                        zipOut.closeEntry();
                    }
                }
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            log.error("批量导出失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("批量导出失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按条件筛选导出")
    @PostMapping("/export/condition")
    public ResponseEntity<?> exportByCondition(
            @RequestParam(required = false) String suffix,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        try {
            List<SysOssFile> files = ossFileService.exportFilesByCondition(suffix, startTime, endTime);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            log.error("条件导出失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("条件导出失败: " + e.getMessage());
        }
    }

    // ==================== 导入端点 ====================

    @Operation(summary = "单文件导入")
    @PostMapping("/import/file")
    public ResponseEntity<?> importFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("configName") String configName) {
        try {
            String url = ossFileService.importFile(file, configName);
            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("size", file.getSize());
            result.put("success", true);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("文件导入失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("文件导入失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量文件导入")
    @PostMapping("/import/batch")
    public ResponseEntity<?> importBatch(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("configName") String configName) {
        try {
            List<Map<String, Object>> results = ossFileService.importFiles(files, configName);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("批量导入失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("批量导入失败: " + e.getMessage());
        }
    }

    // ==================== 跨存储迁移端点 ====================

    @Operation(summary = "单文件迁移")
    @PostMapping("/migrate/file")
    public ResponseEntity<?> migrateFile(
            @RequestParam("ossId") Long ossId,
            @RequestParam("sourceConfig") String sourceConfig,
            @RequestParam("targetConfig") String targetConfig) {
        try {
            SysOssFile migrated = ossFileService.migrateFile(ossId, sourceConfig, targetConfig);
            return ResponseEntity.ok(migrated);
        } catch (Exception e) {
            log.error("文件迁移失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("文件迁移失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量文件迁移")
    @PostMapping("/migrate/batch")
    public ResponseEntity<?> migrateBatch(
            @RequestBody List<Long> ossIds,
            @RequestParam("sourceConfig") String sourceConfig,
            @RequestParam("targetConfig") String targetConfig) {
        try {
            List<Map<String, Object>> results = ossFileService.migrateFiles(ossIds, sourceConfig, targetConfig);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("批量迁移失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("批量迁移失败: " + e.getMessage());
        }
    }

    @Operation(summary = "全量迁移")
    @PostMapping("/migrate/all")
    public ResponseEntity<?> migrateAll(
            @RequestParam("sourceConfig") String sourceConfig,
            @RequestParam("targetConfig") String targetConfig) {
        try {
            List<Map<String, Object>> results = ossFileService.migrateAll(sourceConfig, targetConfig);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("全量迁移失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("全量迁移失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询迁移进度")
    @GetMapping("/migrate/progress/{taskId}")
    public ResponseEntity<?> getMigrateProgress(@PathVariable("taskId") Long taskId) {
        try {
            Map<String, Object> progress = ossFileService.getMigrateProgress(taskId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            log.error("查询迁移进度失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("查询迁移进度失败: " + e.getMessage());
        }
    }
}