package com.example.oss.facade;

/**
 * OSS客户端门面接口
 * 封装新旧两种获取客户端的方式，提供统一访问入口
 */
public interface OssClientFacade {

    /**
     * 获取OSS客户端（兼容新旧两种方式）
     *
     * @param configName 配置名称
     * @return OSS客户端实例
     */
    Object getClient(String configName);

    /**
     * 获取默认OSS客户端
     *
     * @return 默认OSS客户端实例
     */
    Object getDefaultClient();

    /**
     * 测试OSS连接
     *
     * @param configName 配置名称
     * @return 是否连接成功
     */
    boolean testConnection(String configName);
}