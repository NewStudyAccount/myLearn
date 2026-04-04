package com.example.oss.factory;

import com.example.oss.domain.OssConfig;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MinIO客户端工厂
 */
@Slf4j
@Component
public class MinioClientFactory implements OssClientFactory {

    @Override
    public Object createClient(OssConfig ossConfig) {
        try {
            // 创建MinIO客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ossConfig.getEndpoint())
                    .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey())
                    .build();
            
            log.info("MinIO客户端创建成功: endpoint={}, bucket={}", 
                    ossConfig.getEndpoint(), ossConfig.getBucketName());
            
            return minioClient;
        } catch (Exception e) {
            log.error("创建MinIO客户端失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建MinIO客户端失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getProvider() {
        return "minio";
    }
}