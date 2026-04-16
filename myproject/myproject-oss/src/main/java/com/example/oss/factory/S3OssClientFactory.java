package com.example.oss.factory;

import com.example.oss.domain.SysOssConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import jakarta.annotation.PreDestroy;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class S3OssClientFactory implements OssClientFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Cache<String, S3Client> clientCache;

    public S3OssClientFactory() {
        this.clientCache = Caffeine.newBuilder()
                .maximumSize(50)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .removalListener((String key, S3Client client, RemovalCause cause) -> {
                    if (client != null) {
                        try {
                            client.close();
                            log.info("S3客户端已关闭并移除: configName={}, reason={}", key, cause);
                        } catch (Exception e) {
                            log.error("关闭S3客户端失败: configName={}, error={}", key, e.getMessage());
                        }
                    }
                })
                .recordStats()
                .build();

        log.info("S3客户端缓存初始化完成: maxSize=50, expireAfterAccess=30min, expireAfterWrite=24h");
    }

    @PreDestroy
    public void destroy() {
        log.info("开始清理所有S3客户端，当前缓存大小: {}", clientCache.estimatedSize());
        clientCache.asMap().forEach((key, client) -> {
            try {
                client.close();
                log.info("S3客户端已关闭: configName={}", key);
            } catch (Exception e) {
                log.error("关闭S3客户端失败: configName={}, error={}", key, e.getMessage());
            }
        });
        clientCache.invalidateAll();
        log.info("所有S3客户端已清理完成");
    }
    @Override
    public Object createClient(SysOssConfig sysOssConfig) {
        return getClient(sysOssConfig);
    }

    private S3Client getClient(SysOssConfig sysOssConfig) {
        String cacheKey = sysOssConfig.getConfigName();

        return clientCache.get(cacheKey, key -> {
            try {
                S3ClientBuilder builder = S3Client.builder()
                        .endpointOverride(URI.create(sysOssConfig.getEndpoint()))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(sysOssConfig.getAccessKey(), sysOssConfig.getSecretKey())
                        ));

                if (sysOssConfig.getRegion() != null && !sysOssConfig.getRegion().isEmpty()) {
                    builder.region(Region.of(sysOssConfig.getRegion()));
                } else {
                    builder.region(Region.of("us-east-1"));
                }

                applyExtraConfig(builder, sysOssConfig.getExtraConfig());

                S3Client s3Client = builder.build();
                log.info("S3客户端创建成功: endpoint={}, bucket={}", sysOssConfig.getEndpoint(), sysOssConfig.getBucketName());
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
    public void uploadFile(SysOssConfig sysOssConfig, String objectName, String contentType, byte[] data) {
        try {
            S3Client s3Client = getClient(sysOssConfig);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(sysOssConfig.getBucketName())
                    .key(objectName)
                    //设置contenType、contentDisposition 保证浏览器能直接显示 图片
                    .contentType(contentType)
                    .contentDisposition("inline")
                    .build();
            s3Client.putObject(request, RequestBody.fromBytes(data));
            log.info("S3文件上传成功: bucket={}, object={}", sysOssConfig.getBucketName(), objectName);
        } catch (Exception e) {
            log.error("S3文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("S3文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] downloadFile(SysOssConfig sysOssConfig, String objectName) {
        try {
            S3Client s3Client = getClient(sysOssConfig);
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(sysOssConfig.getBucketName())
                    .key(objectName)
                    .build();
            try (InputStream in = s3Client.getObject(request);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                log.info("S3文件下载成功: bucket={}, object={}", sysOssConfig.getBucketName(), objectName);
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
        clientCache.invalidate(configName);
        log.info("S3客户端缓存已失效: configName={}", configName);
    }

    public void clearAllClients() {
        clientCache.invalidateAll();
        log.info("所有S3客户端缓存已清空");
    }

    public long getCacheSize() {
        return clientCache.estimatedSize();
    }

    public CacheStats getCacheStats() {
        return clientCache.stats();
    }

    public void printCacheStats() {
        var stats = clientCache.stats();
        log.info("S3客户端缓存统计 - 大小: {}, 命中率: {}%, 命中次数: {}, 未命中次数: {}, 驱逐次数: {}",
                clientCache.estimatedSize(),
                String.format("%.2f", stats.hitRate() * 100),
                stats.hitCount(),
                stats.missCount(),
                stats.evictionCount());
    }
}
