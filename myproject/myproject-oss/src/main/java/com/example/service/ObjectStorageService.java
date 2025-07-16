package com.example.service;

public interface ObjectStorageService {
    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param objectKey  对象键（文件路径）
     * @param filePath   文件本地路径
     */
    void uploadFile(String bucketName, String objectKey, String filePath);

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param objectKey  对象键（文件路径）
     * @param filePath   下载到的本地路径
     */
    void downloadFile(String bucketName, String objectKey, String filePath);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectKey  对象键（文件路径）
     */
    void deleteFile(String bucketName, String objectKey);

    /**
     * 获取文件访问URL（临时或公开）
     *
     * @param bucketName 存储桶名称
     * @param objectKey  对象键（文件路径）
     * @param expireTime 过期时间（单位：秒）
     * @return 访问链接
     */
    String getPresignedUrl(String bucketName, String objectKey, int expireTime);
}
