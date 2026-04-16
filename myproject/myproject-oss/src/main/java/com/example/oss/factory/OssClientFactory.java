package com.example.oss.factory;

import com.example.oss.domain.SysOssConfig;

/**
 * OSS客户端工厂接口
 */
public interface OssClientFactory {

    /**
     * 根据配置创建OSS客户端
     *
     * @param sysOssConfig OSS配置
     * @return OSS客户端实例
     */
    Object createClient(SysOssConfig sysOssConfig);

    /**
     * 获取支持的提供商类型
     *
     * @return 提供商类型
     */
    String getProvider();


    /**
     * 上传文件
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     * @param data       文件数据
     * @return 对象的访问路径或key
     */
    void uploadFile(SysOssConfig sysOssConfig, String objectName, String contentType, byte[] data);

    /**
     * 下载文件
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     * @return 文件数据
     */
    byte[] downloadFile(SysOssConfig sysOssConfig, String objectName);
}