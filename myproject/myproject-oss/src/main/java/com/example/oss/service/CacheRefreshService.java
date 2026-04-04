package com.example.oss.service;

/**
 * 缓存刷新服务接口
 */
public interface CacheRefreshService {

    /**
     * 刷新所有OSS配置缓存
     */
    void refreshAllOssConfigCache();
}