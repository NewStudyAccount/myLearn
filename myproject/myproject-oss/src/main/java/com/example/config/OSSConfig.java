package com.example.config;

import com.aliyun.oss.OSSClient;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    /**
     * 阿里云OSS客户端
     *
     * @param ossProperties
     * @return
     */
    @Bean
    public OSSClient ossClient(OSSProperties ossProperties) {
        return new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }


    /**
     * MinIO客户端
     *
     * @param ossProperties
     * @return
     */
    @Bean
    public MinioClient minioClient(OSSProperties ossProperties) {
        return new MinioClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }

}
