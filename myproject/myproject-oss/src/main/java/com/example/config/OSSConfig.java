package com.example.config;

import com.sun.net.httpserver.HttpsConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class OSSConfig {

    /**
     * 阿里云OSS客户端
     *
     * @param ossProperties
     * @return
     */
//    @Bean
//    public OSSClient ossClient(OSSProperties ossProperties) {
//        return new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
//    }


    /**
     * MinIO客户端
     *
     * @param
     * @return
     */
//    @Bean
//    public MinioClient minioClient(OSSProperties ossProperties) {
//        return new MinioClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
//    }


    //    qjj-learn
//    accessKey  LTAI5tSJf7HTwMp2ZkdENjgT
//    AccessKey Secret   jpvy38MQpYfC6suwb3Q5KyHxBqNn1C
//    oss-cn-shanghai

    @Bean
    public S3Client s3Client() {

        return S3Client.builder()
                .endpointOverride(URI.create("https://oss-cn-shanghai.aliyuncs.com"))
                .region(Region.of("oss-cn-shanghai"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("LTAI5tSJf7HTwMp2ZkdENjgT", "jpvy38MQpYfC6suwb3Q5KyHxBqNn1C")
                ))
//              非亚马逊云的需要如下配置 如果你不是上传到 AWS 官方 S3，建议禁用 chunked encoding 或设置 x-amz-content-sha256: UNSIGNED-PAYLOAD 来规避该错误。
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(false)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }





}
