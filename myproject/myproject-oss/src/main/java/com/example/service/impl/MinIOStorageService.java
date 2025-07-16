package com.example.service.impl;

import com.example.service.ObjectStorageService;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinIOStorageService implements ObjectStorageService {

    @Autowired
    private MinioClient minioClient;

    public MinIOStorageService(
            @Value("${minio.endpoint}") String endpoint,
            @Value("${minio.accessKey}") String accessKey,
            @Value("${minio.secretKey}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public void uploadFile(String bucketName, String objectKey, String filePath) {
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .filename(filePath)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload failed", e);
        }
    }

    @Override
    public void downloadFile(String bucketName, String objectKey, String filePath) {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .filename(filePath)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO download failed", e);
        }
    }

    @Override
    public void deleteFile(String bucketName, String objectKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO delete failed", e);
        }
    }

    @Override
    public String getPresignedUrl(String bucketName, String objectKey, int expireTime) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectKey)
                            .expiry(expireTime)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("MinIO generate URL failed", e);
        }
    }
}
