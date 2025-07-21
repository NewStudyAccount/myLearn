package com.example.config;

import org.springframework.context.annotation.Configuration;

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



//    @Bean
//    public S3AsyncClient s3AsyncClient() {
//        return S3AsyncClient.builder()
//                .endpointOverride(URI.create("https://oss-cn-shanghai.aliyuncs.com"))
//                .region(Region.of("oss-cn-shanghai"))
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create("LTAI5tSJf7HTwMp2ZkdENjgT", "jpvy38MQpYfC6suwb3Q5KyHxBqNn1C")
//                ))
//                .serviceConfiguration(S3Configuration.builder()
//                        .pathStyleAccessEnabled(false)
//                        .chunkedEncodingEnabled(false) // ❗关键：禁用 chunk 编码
//                        .checksumValidationEnabled(false) // ❗禁用校验，避免 x-amz-content-sha256 报错
//                        .build())
//                .build();
//    }





}
