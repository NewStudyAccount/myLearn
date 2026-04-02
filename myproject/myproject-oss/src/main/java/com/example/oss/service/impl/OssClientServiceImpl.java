package com.example.oss.service.impl;

import com.example.oss.domain.OssConfig;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.factory.OssClientFactoryProvider;
import com.example.oss.service.OssClientService;
import com.example.oss.service.OssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * OSS客户端服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssClientServiceImpl implements OssClientService {

    private final OssConfigService ossConfigService;
    private final OssClientFactoryProvider factoryProvider;

    @Override
    public Object getClient(String configName) {
        // 获取配置
        OssConfig ossConfig = ossConfigService.getByConfigName(configName);
        if (ossConfig == null) {
            throw new RuntimeException("OSS配置不存在: " + configName);
        }
        
        // 检查配置是否启用
        if (!Boolean.TRUE.equals(ossConfig.getIsActive())) {
            throw new RuntimeException("OSS配置未启用: " + configName);
        }
        
        // 获取对应的工厂
        OssClientFactory factory = factoryProvider.getFactory(ossConfig.getProvider());
        
        // 创建客户端
        return factory.createClient(ossConfig);
    }

    @Override
    public boolean testConnection(String configName) {
        try {
            // 获取客户端（这会触发客户端创建）
            Object client = getClient(configName);
            
            // 根据客户端类型进行连接测试
            if (client instanceof com.aliyun.oss.OSS) {
                // 阿里云OSS客户端测试
                com.aliyun.oss.OSS ossClient = (com.aliyun.oss.OSS) client;
                // 尝试列出存储桶（简单测试）
                ossClient.listBuckets();
                log.info("阿里云OSS连接测试成功: {}", configName);
                return true;
            } else if (client instanceof io.minio.MinioClient) {
                // MinIO客户端测试
                io.minio.MinioClient minioClient = (io.minio.MinioClient) client;
                // 尝试列出存储桶（简单测试）
                minioClient.listBuckets();
                log.info("MinIO连接测试成功: {}", configName);
                return true;
            } else {
                log.warn("未知的客户端类型，无法测试连接: {}", client.getClass().getName());
                return false;
            }
        } catch (Exception e) {
            log.error("OSS连接测试失败: configName={}, error={}", configName, e.getMessage(), e);
            return false;
        }
    }
}