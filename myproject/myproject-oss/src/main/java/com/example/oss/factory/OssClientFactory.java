package com.example.oss.factory;

import com.example.oss.domain.SysOssConfig;

import java.io.InputStream;

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
     * @param contentType 内容类型
     * @param data       文件数据
     */
    void uploadFile(SysOssConfig sysOssConfig, String objectName, String contentType, byte[] data);

    /**
     * 上传文件流
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     * @param contentType 内容类型
     * @param inputStream 文件输入流
     * @param contentLength 文件大小
     */
    void uploadFile(SysOssConfig sysOssConfig, String objectName, String contentType, InputStream inputStream, long contentLength);

    /**
     * 下载文件
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     * @return 文件数据
     */
    byte[] downloadFile(SysOssConfig sysOssConfig, String objectName);

    /**
     * 下载文件流
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     * @return 文件输入流
     */
    InputStream downloadFileStream(SysOssConfig sysOssConfig, String objectName);

    /**
     * 删除文件
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     */
    void deleteFile(SysOssConfig sysOssConfig, String objectName);

    /**
     * 获取文件大小
     *
     * @param sysOssConfig  OSS配置
     * @param objectName 对象名称
     * @return 文件字节大小
     */
    long getFileSize(SysOssConfig sysOssConfig, String objectName);
}