package com.example.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oss.domain.OssConfig;

import java.util.List;

/**
 * OSS配置服务接口
 */
public interface OssConfigService extends IService<OssConfig> {

    /**
     * 根据配置名称获取配置
     *
     * @param configName 配置名称
     * @return OSS配置
     */
    OssConfig getByConfigName(String configName);

    /**
     * 获取所有激活的配置
     *
     * @return 激活的配置列表
     */
    List<OssConfig> listActive();

    /**
     * 创建配置
     *
     * @param ossConfig 配置信息
     * @return 创建后的配置
     */
    OssConfig create(OssConfig ossConfig);

    /**
     * 更新配置
     *
     * @param ossConfig 配置信息
     * @return 更新后的配置
     */
    OssConfig update(OssConfig ossConfig);

    /**
     * 删除配置
     *
     * @param id 配置ID
     * @return 是否删除成功
     */
    boolean delete(Long id);
}