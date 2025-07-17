//package com.example.service.impl;
//
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.model.GeneratePresignedUrlRequest;
//import com.example.service.ObjectStorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.net.URL;
//
//@Service
//public class AliyunOSSService implements ObjectStorageService {
//
//
//    @Autowired
//    private OSSClient ossClient;
//
//
//    @Override
//    public void uploadFile(String bucketName, String objectKey, String filePath) {
//        ossClient.putObject(bucketName, objectKey, new File(filePath));
//    }
//
//    @Override
//    public void downloadFile(String bucketName, String objectKey, String filePath) {
//        ossClient.getObject(new com.aliyun.oss.model.GetObjectRequest(bucketName, objectKey), new File(filePath));
//    }
//
//    @Override
//    public void deleteFile(String bucketName, String objectKey) {
//        ossClient.deleteObject(bucketName, objectKey);
//    }
//
//    @Override
//    public String getPresignedUrl(String bucketName, String objectKey, int expireTime) {
//        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey);
//        URL url = ossClient.generatePresignedUrl(request);
//        return url.toString();
//    }
//}
