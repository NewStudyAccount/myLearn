package com.example.oss.controller;

import com.example.oss.domain.OssConfig;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.factory.OssClientFactoryProvider;
import com.example.oss.service.OssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * OSS文件操作控制器
 */
@Slf4j
@RestController
@RequestMapping("/project/oss")
@RequiredArgsConstructor
public class OssController {

    private final OssConfigService ossConfigService;
    private final OssClientFactoryProvider factoryProvider;

    private final OssClientFactory ossClientFactory;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("configName") String configName,
                                    @RequestParam("file") MultipartFile file) {
        try {
            OssConfig ossConfig = getActiveConfig(configName);
//            OssClientFactory factory = factoryProvider.getFactory(ossConfig.getProvider());
            String objectName = ossClientFactory.uploadFile(ossConfig, file.getOriginalFilename(), file.getBytes());

            Map<String, Object> result = new HashMap<>();
            result.put("objectName", objectName);
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
    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("configName") String configName,
                                      @RequestParam("objectName") String objectName) {
        try {
            OssConfig ossConfig = getActiveConfig(configName);
//            OssClientFactory factory = factoryProvider.getFactory(ossConfig.getProvider());
            byte[] data = ossClientFactory.downloadFile(ossConfig, objectName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
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
