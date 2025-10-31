package com.example.config;

import com.example.domain.SysOssConfig;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
     * @param fileName
     * @param inputStream
     * @return
     */
    public String uploadFile(String fileName, InputStream inputStream) {
        try {
            s3AsyncClient.putObject(PutObjectRequest.builder()
                            .bucket(sysOssConfig.getBucketName())
                            .key(sysOssConfig.getFileFolder()+"/"+ fileName)
                            .acl("public-read")
                            .build(),
                    AsyncRequestBody.fromBytes(inputStream.readAllBytes())).join();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getUrl(fileName);
    }


    /**
     * 大文件上传
     * @param fileName
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String uploadBigFileFromStream(String fileName, InputStream inputStream) throws IOException {

        S3TransferManager transferManager = S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .uploadDirectoryMaxDepth(1).build();

        Path tempFile = Files.createTempFile("upload", ".tmp");
        try (inputStream) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(b -> b.bucket(sysOssConfig.getBucketName()).key(sysOssConfig.getFileFolder()+"/"+fileName))
                .source(tempFile)
                .build();

        FileUpload upload = transferManager.uploadFile(uploadFileRequest);
        upload.completionFuture().join();

        Files.deleteIfExists(tempFile); // 清理临时文件
        return getUrl(fileName);
    }

    public String getUrl(String fileName) {
        // 阿里云公网域名： https://<bucket>.<endpoint>/<key>
        return String.format("https://%s.%s/%s/%s",
                sysOssConfig.getBucketName(),
                sysOssConfig.getEndPoint()
                        .replaceFirst("https?://", ""),
                sysOssConfig.getFileFolder(),
                fileName);
    }

    /**
     * 检查配置是否相同
     */
    public boolean checkPropertiesSame(SysOssConfig sysOssConfig) {
        return this.sysOssConfig.equals(sysOssConfig);
    }


}
