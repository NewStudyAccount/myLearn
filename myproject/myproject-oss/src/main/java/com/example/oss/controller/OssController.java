package com.example.oss.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssFileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OSS文件操作控制器
 */
@Slf4j
@RestController
@RequestMapping("/project/sysOssFile")
@RequiredArgsConstructor
public class OssController {

    private final OssConfigService ossConfigService;

    private final OssClientFactory ossClientFactory;


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
            byte[] data = ossClientFactory.downloadFile(sysOssConfig, fileName);

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




}
