package com.example.oss.cache;

import com.example.oss.domain.OssConfig;

import java.util.List;

/**
 * OSS配置缓存服务接口
 */
public interface OssConfigCacheService {

    /**
     * 获取缓存的配置
     *
     * @param configName 配置名称
     * @return 缓存的配置，如果不存在返回null
     */
    OssConfig getConfig(String configName);

    /**
     * 缓存配置
     *
     * @param configName 配置名称
     * @param ossConfig 配置对象
     */
    void putConfig(String configName, OssConfig ossConfig);

    /**
     * 删除缓存的配置
     *
     * @param configName 配置名称
     */
    void evictConfig(String configName);

    /**
     * 获取所有激活配置的缓存
     *
     * @return 激活配置列表，如果不存在返回null
     */
    List<OssConfig> getActiveConfigs();

    /**
     * 缓存所有激活配置
     *
     * @param activeConfigs 激活配置列表
     */
    void putActiveConfigs(List<OssConfig> activeConfigs);

    /**
     * 删除所有激活配置缓存
     */
    void evictActiveConfigs();

    /**
     * 清除所有OSS配置相关缓存
     */
    void clearAll();
}