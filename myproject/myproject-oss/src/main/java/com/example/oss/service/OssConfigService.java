package com.example.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.req.sysOssConfig.SysOssConfigQueryPageReq;

import java.util.List;

/**
 * OSS配置服务接口
 */
public interface OssConfigService extends IService<SysOssConfig> {


    void initConfig();


    /**
     * 根据配置名称获取配置
     *
     * @param configName 配置名称
     * @return OSS配置
     */
    SysOssConfig getByConfigName(String configName);

    /**
     * 获取所有激活的配置
     *
     * @return 激活的配置列表
     */
    List<SysOssConfig> listActive();

    /**
     * 创建配置
     *
     * @param sysOssConfig 配置信息
     * @return 创建后的配置
     */
    SysOssConfig create(SysOssConfig sysOssConfig);

    /**
     * 更新配置
     *
     * @param sysOssConfig 配置信息
     * @return 更新后的配置
     */
    SysOssConfig update(SysOssConfig sysOssConfig);

    /**
     * 删除配置
     *
     * @param id 配置ID
     * @return 是否删除成功
     */
    boolean delete(Long id);


    public TableDataInfo<SysOssConfig> querySysOssConfigListPage(SysOssConfigQueryPageReq pageReq);
}