package com.example.oss.service;

/**
 * OSS客户端服务接口
 */
public interface OssClientService {

    /**
     * 根据配置名称获取OSS客户端
     *
     * @param configName 配置名称
     * @return OSS客户端实例
     */
    Object getClient(String configName);

    /**
     * 测试OSS连接
     *
     * @param configName 配置名称
     * @return 是否连接成功
     */
    boolean testConnection(String configName);
}