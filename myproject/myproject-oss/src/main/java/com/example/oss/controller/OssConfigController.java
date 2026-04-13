package com.example.oss.controller;

import com.example.oss.factory.S3OssClientFactory;
import com.example.oss.service.OssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/project/oss/config")
@RequiredArgsConstructor
public class OssConfigController {

    @Autowired
    private OssConfigService ossConfigService;

    @Autowired(required = false)
    private S3OssClientFactory s3OssClientFactory;

    @PostMapping("/refresh")
    public String refreshCache() {
        log.info("收到刷新OSS配置缓存请求");
        ossConfigService.initConfig();
        return "OSS配置缓存刷新成功";
    }

    @GetMapping("/cache/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();

        if (s3OssClientFactory != null) {
            var cacheStats = s3OssClientFactory.getCacheStats();
            stats.put("cacheSize", s3OssClientFactory.getCacheSize());
            stats.put("hitRate", String.format("%.2f%%", cacheStats.hitRate() * 100));
            stats.put("hitCount", cacheStats.hitCount());
            stats.put("missCount", cacheStats.missCount());
            stats.put("evictionCount", cacheStats.evictionCount());
            stats.put("loadSuccessCount", cacheStats.loadSuccessCount());
            stats.put("loadFailureCount", cacheStats.loadFailureCount());
            stats.put("averageLoadPenalty", String.format("%.2fms", cacheStats.averageLoadPenalty() / 1_000_000.0));
        } else {
            stats.put("message", "S3客户端工厂未启用");
        }

        return stats;
    }

    @PostMapping("/cache/clear")
    public String clearCache() {
        if (s3OssClientFactory != null) {
            s3OssClientFactory.clearAllClients();
            return "S3客户端缓存已清空";
        }
        return "S3客户端工厂未启用";
    }
}
