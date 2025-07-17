package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OssService {

    @Autowired
    private S3Client s3Client;
    private final String bucketName = "qjj-learn";



    /**
     * 上传文件到 S3
     *
     * @param key        S3 中的文件键（路径+文件名）
     * @param inputStream 文件输入流
     * @return 返回上传成功的提示信息
     */
    public String uploadFile(String key, InputStream inputStream) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("application/octet-stream") // 可根据文件类型修改
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream,readStreamFully(inputStream).length));
//            s3Client.putObject(request, RequestBody.fromBytes(inputStream.readAllBytes()));
            return "File uploaded: " + key;

        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readStreamFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int read;
        while ((read = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }
        return buffer.toByteArray();
    }
}