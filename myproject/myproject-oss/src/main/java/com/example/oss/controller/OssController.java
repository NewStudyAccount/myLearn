package com.example.oss.controller;

import com.example.oss.domain.OssConfig;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssFileService;
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
@RequestMapping("/project/oss/file")
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
            List<OssConfig> ossConfigs = ossConfigService.listActive();
            if (CollectionUtils.isEmpty(ossConfigs)) {
                return ResponseEntity.badRequest().body("未找到有效的OSS配置");
            }
            OssConfig ossConfig = ossConfigs.getFirst();
            byte[] data = ossClientFactory.downloadFile(ossConfig, fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("文件下载失败: " + e.getMessage());
        }
    }

    private OssConfig getActiveConfig(String configName) {
        OssConfig ossConfig = ossConfigService.getByConfigName(configName);
        if (ossConfig == null) {
            throw new RuntimeException("OSS配置不存在: " + configName);
        }
        if (!Boolean.TRUE.equals(ossConfig.getIsActive())) {
            throw new RuntimeException("OSS配置未启用: " + configName);
        }
        return ossConfig;
    }
}
