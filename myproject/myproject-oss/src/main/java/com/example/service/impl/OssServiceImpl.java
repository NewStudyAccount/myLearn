package com.example.service.impl;

import com.example.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
public class OssServiceImpl implements ObjectStorageService {


    @Autowired
    private S3AsyncClient s3AsyncClient;


    @Override
    public void uploadFile(String bucketName, String objectKey,InputStream inputStream) {

        try {
            s3AsyncClient.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build(),
                    AsyncRequestBody.fromBytes(inputStream.readAllBytes())).join().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadFile(String bucketName, String objectKey, String filePath) {

    }

    @Override
    public void deleteFile(String bucketName, String objectKey) {

    }

    @Override
    public String getPresignedUrl(String bucketName, String objectKey, int expireTime) {
        return "";
    }
}
