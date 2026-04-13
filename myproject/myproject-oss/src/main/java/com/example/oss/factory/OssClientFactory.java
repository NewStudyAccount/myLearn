package com.example.oss.factory;

import com.example.oss.domain.OssConfig;

/**
 * OSS客户端工厂接口
 */
public interface OssClientFactory {

    /**
     * 根据配置创建OSS客户端
     *
     * @param ossConfig OSS配置
     * @return OSS客户端实例
     */
    Object createClient(OssConfig ossConfig);

    /**
     * 获取支持的提供商类型
     *
     * @return 提供商类型
     */
    String getProvider();


    /**
     * 上传文件
     *
     * @param ossConfig  OSS配置
     * @param objectName 对象名称
     * @param data       文件数据
     * @return 对象的访问路径或key
     */
    void uploadFile(OssConfig ossConfig, String objectName,String contentType, byte[] data);

    /**
     * 下载文件
     *
     * @param ossConfig  OSS配置
     * @param objectName 对象名称
     * @return 文件数据
     */
    byte[] downloadFile(OssConfig ossConfig, String objectName);
}