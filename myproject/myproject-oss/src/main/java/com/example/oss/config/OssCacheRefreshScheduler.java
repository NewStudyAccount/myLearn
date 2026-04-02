package com.example.oss.config;

import com.example.oss.service.CacheRefreshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * OSS配置缓存定时刷新任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "oss.cache.refresh.enabled", havingValue = "true", matchIfMissing = false)
public class OssCacheRefreshScheduler {

    private final CacheRefreshService cacheRefreshService;

    /**
     * 每30分钟刷新一次OSS配置缓存
     * 可以通过配置文件 oss.cache.refresh.cron 自定义cron表达式
     */
    @Scheduled(fixedRate = 30 * 60 * 1000) // 30分钟
    public void refreshCache() {
        log.info("定时刷新OSS配置缓存开始");
        try {
            cacheRefreshService.refreshAllOssConfigCache();
            log.info("定时刷新OSS配置缓存完成");
        } catch (Exception e) {
            log.error("定时刷新OSS配置缓存失败: {}", e.getMessage(), e);
        }
    }
}