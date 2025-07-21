package com.example.config;

import com.example.domain.SysOssConfig;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class OssClient {

    private final S3AsyncClient s3AsyncClient;

    private final SysOssConfig sysOssConfig;

    public OssClient(SysOssConfig sysOssConfig) {
        this.sysOssConfig = sysOssConfig;

        this.s3AsyncClient = S3AsyncClient.builder()
                .endpointOverride(URI.create(sysOssConfig.getEndPoint()))
                .region(Region.of(sysOssConfig.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(sysOssConfig.getAccessKey(), sysOssConfig.getKeySecret())
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(false)
                        .chunkedEncodingEnabled(false) // ❗关键：禁用 chunk 编码
                        .checksumValidationEnabled(false) // ❗禁用校验，避免 x-amz-content-sha256 报错
                        .build())
                .build();
    }


    /**
     * 上传文件，并返回访问URL
     * @param key
     * @param inputStream
     * @return
     */
    public String uploadFile(String key, InputStream inputStream) {
        try {
            s3AsyncClient.putObject(PutObjectRequest.builder()
                            .bucket(sysOssConfig.getBucketName())
                            .key(key)
                            .acl("public-read")
                            .build(),
                    AsyncRequestBody.fromBytes(inputStream.readAllBytes())).join();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getUrl( key);
    }

    public String getUrl(String key) {
        // 阿里云公网域名： https://<bucket>.<endpoint>/<key>
        return String.format("https://%s.%s/%s",
                sysOssConfig.getBucketName(),
                sysOssConfig.getEndPoint()
                        .replaceFirst("https?://", ""),
                key);
    }

    /**
     * 检查配置是否相同
     */
    public boolean checkPropertiesSame(SysOssConfig sysOssConfig) {
        return this.sysOssConfig.equals(sysOssConfig);
    }


}
