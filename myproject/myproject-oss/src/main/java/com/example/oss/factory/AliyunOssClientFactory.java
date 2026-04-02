package com.example.oss.factory;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.oss.domain.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS客户端工厂
 */
@Slf4j
@Component
public class AliyunOssClientFactory implements OssClientFactory {

    @Override
    public Object createClient(OssConfig ossConfig) {
        try {
            // 创建阿里云OSS客户端
            OSS ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKey(),
                    ossConfig.getSecretKey()
            );
            
            log.info("阿里云OSS客户端创建成功: endpoint={}, bucket={}", 
                    ossConfig.getEndpoint(), ossConfig.getBucketName());
            
            return ossClient;
        } catch (Exception e) {
            log.error("创建阿里云OSS客户端失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建阿里云OSS客户端失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getProvider() {
        return "aliyun";
    }
}