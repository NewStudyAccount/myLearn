package com.example.oss.service.impl;

import com.example.oss.cache.OssConfigCacheService;
import com.example.oss.service.CacheRefreshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 缓存刷新服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheRefreshServiceImpl implements CacheRefreshService {

    private final OssConfigCacheService cacheService;

    @Override
    public void refreshAllOssConfigCache() {
        log.info("开始刷新所有OSS配置缓存");
        cacheService.clearAll();
        log.info("刷新所有OSS配置缓存完成");
    }
}