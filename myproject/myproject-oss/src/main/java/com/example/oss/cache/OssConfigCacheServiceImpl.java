package com.example.oss.cache;

import com.example.oss.domain.OssConfig;
import com.example.redis.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * OSS配置缓存服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssConfigCacheServiceImpl implements OssConfigCacheService {

    private final RedisCache redisCache;

    private static final String CONFIG_KEY_PREFIX = "oss:config:";
    private static final String ACTIVE_CONFIGS_KEY = "oss:config:active";
    private static final long CACHE_TIMEOUT = 5; // 5分钟
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    @Override
    public OssConfig getConfig(String configName) {
        String key = CONFIG_KEY_PREFIX + configName;
        return redisCache.getCacheObject(key);
    }

    @Override
    public void putConfig(String configName, OssConfig ossConfig) {
        String key = CONFIG_KEY_PREFIX + configName;
        redisCache.setCacheObject(key, ossConfig, (int) CACHE_TIMEOUT, TIME_UNIT);
        log.debug("缓存OSS配置: configName={}", configName);
    }

    @Override
    public void evictConfig(String configName) {
        String key = CONFIG_KEY_PREFIX + configName;
        redisCache.deleteObject(key);
        log.debug("清除OSS配置缓存: configName={}", configName);
    }

    @Override
    public List<OssConfig> getActiveConfigs() {
        return redisCache.getCacheObject(ACTIVE_CONFIGS_KEY);
    }

    @Override
    public void putActiveConfigs(List<OssConfig> activeConfigs) {
        redisCache.setCacheObject(ACTIVE_CONFIGS_KEY, activeConfigs, (int) CACHE_TIMEOUT, TIME_UNIT);
        log.debug("缓存所有激活OSS配置: count={}", activeConfigs != null ? activeConfigs.size() : 0);
    }

    @Override
    public void evictActiveConfigs() {
        redisCache.deleteObject(ACTIVE_CONFIGS_KEY);
        log.debug("清除所有激活OSS配置缓存");
    }

    @Override
    public void clearAll() {
        // 清除所有OSS配置相关缓存
        // 注意：这里只是清除了已知的键，如果需要清除所有匹配模式的键，可以使用keys方法
        // 但生产环境慎用keys方法，可能影响性能
        evictActiveConfigs();
        log.info("清除所有OSS配置缓存");
    }
}