package com.example.config;

import io.minio.messages.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;

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
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(readStreamFully(inputStream)));
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


//





    public String uploadFile4(String key, InputStream inputStream) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            byte[] content = inputStream.readAllBytes();

            s3Client.putObject(request, RequestBody.fromByteBuffer(ByteBuffer.wrap(content)));
            return "File uploaded: " + key;

        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ListBucketsResponse listAllBuckets() {
        ListBucketsResponse ListBucketsResponse = s3Client.listBuckets();

        return ListBucketsResponse;
    }


//      v1版本的
//    private TransferManager transferManager;



    public String uploadFile2(String key, InputStream inputStream) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/octet-stream")
                .build();

        s3Client.putObject(request, AsyncRequestBody.fromInputStream(inputStream, contentLength, executorService));
    }





//    使用分片上传（适合大文件）
    public String uploadBigFiles(String key, InputStream inputStream) {

        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CreateMultipartUploadResponse createResponse = s3Client.createMultipartUpload(createRequest);
        String uploadId = createResponse.uploadId();

// 然后依次上传每个 part，最后 CompleteMultipartUpload

    }


//    使用 TransferManager（推荐大文件场景



}