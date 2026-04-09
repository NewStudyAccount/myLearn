package com.example.oss.factory;

import com.example.oss.domain.OssConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * S3协议兼容的OSS客户端工厂
 */
@Slf4j
@Component
public class S3OssClientFactory implements OssClientFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, S3Client> clientCache = new ConcurrentHashMap<>();

    @Override
    public Object createClient(OssConfig ossConfig) {
        return getClient(ossConfig);
    }

    private S3Client getClient(OssConfig ossConfig) {
        String cacheKey = ossConfig.getConfigName();

        return clientCache.computeIfAbsent(cacheKey, key -> {
            try {
                S3ClientBuilder builder = S3Client.builder()
                        .endpointOverride(URI.create(ossConfig.getEndpoint()))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(ossConfig.getAccessKey(), ossConfig.getSecretKey())
                        ));

                if (ossConfig.getRegion() != null && !ossConfig.getRegion().isEmpty()) {
                    builder.region(Region.of(ossConfig.getRegion()));
                } else {
                    builder.region(Region.of("us-east-1"));
                }

                applyExtraConfig(builder, ossConfig.getExtraConfig());

                S3Client s3Client = builder.build();
                log.info("S3客户端创建成功: endpoint={}, bucket={}", ossConfig.getEndpoint(), ossConfig.getBucketName());
                return s3Client;
            } catch (Exception e) {
                log.error("创建S3客户端失败: {}", e.getMessage(), e);
                throw new RuntimeException("创建S3客户端失败: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public String getProvider() {
        return "s3";
    }

    @Override
    public String uploadFile(OssConfig ossConfig, String objectName, byte[] data) {
        try {
            S3Client s3Client = getClient(ossConfig);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(ossConfig.getBucketName())
                    .key(objectName)
                    .build();
            s3Client.putObject(request, RequestBody.fromBytes(data));
            log.info("S3文件上传成功: bucket={}, object={}", ossConfig.getBucketName(), objectName);
            return objectName;
        } catch (Exception e) {
            log.error("S3文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("S3文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] downloadFile(OssConfig ossConfig, String objectName) {
        try {
            S3Client s3Client = getClient(ossConfig);
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(ossConfig.getBucketName())
                    .key(objectName)
                    .build();
            try (InputStream in = s3Client.getObject(request);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                log.info("S3文件下载成功: bucket={}, object={}", ossConfig.getBucketName(), objectName);
                return out.toByteArray();
            }
        } catch (Exception e) {
            log.error("S3文件下载失败: {}", e.getMessage(), e);
            throw new RuntimeException("S3文件下载失败: " + e.getMessage(), e);
        }
    }

    private void applyExtraConfig(S3ClientBuilder builder, String extraConfig) {
        if (extraConfig == null || extraConfig.isEmpty()) {
            return;
        }
        try {
            JsonNode node = objectMapper.readTree(extraConfig);
            if (node.has("pathStyleAccess") && node.get("pathStyleAccess").asBoolean()) {
                builder.forcePathStyle(true);
            }
        } catch (Exception e) {
            log.warn("解析extraConfig失败: {}", e.getMessage());
        }
    }

    public void evictClient(String configName) {
        S3Client client = clientCache.remove(configName);
        if (client != null) {
            try {
                client.close();
                log.info("S3客户端已关闭并移除缓存: configName={}", configName);
            } catch (Exception e) {
                log.error("关闭S3客户端失败: {}", e.getMessage(), e);
            }
        }
    }

    public void clearAllClients() {
        clientCache.forEach((configName, client) -> {
            try {
                client.close();
                log.info("S3客户端已关闭: configName={}", configName);
            } catch (Exception e) {
                log.error("关闭S3客户端失败: configName={}, error={}", configName, e.getMessage());
            }
        });
        clientCache.clear();
        log.info("所有S3客户端缓存已清空");
    }
}
