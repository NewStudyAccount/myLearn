package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class OssService {

//    @Autowired
//    private S3Client s3Client;
    private final String bucketName = "qjj-learn";
    @Autowired
    private S3AsyncClient s3AsyncClient;


    /**
     * 上传文件到 S3
     *
     * @param key        S3 中的文件键（路径+文件名）
     * @param inputStream 文件输入流
     * @return 返回上传成功的提示信息
     */
//    public String uploadFile(String key, InputStream inputStream) {
//        try {
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(key)
//                    .build();
//
//            s3Client.putObject(request, RequestBody.fromBytes(readStreamFully(inputStream)));
//            return "File uploaded: " + key;
//
//        } catch (S3Exception e) {
//            throw new RuntimeException("Failed to upload file to S3", e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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





//    public String uploadFile4(String key, InputStream inputStream) {
//        try {
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(key)
//                    .build();
//            byte[] content = inputStream.readAllBytes();
//
//            s3Client.putObject(request, RequestBody.fromByteBuffer(ByteBuffer.wrap(content)));
//            return "File uploaded: " + key;
//
//        } catch (S3Exception e) {
//            throw new RuntimeException("Failed to upload file to S3", e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public ListBucketsResponse listAllBuckets() {
//        ListBucketsResponse ListBucketsResponse = s3Client.listBuckets();
//
//        return ListBucketsResponse;
//    }



    public String uploadFile(String key, InputStream inputStream) throws IOException {

        return s3AsyncClient.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl("public-read")
                .build(),
                AsyncRequestBody.fromBytes(inputStream.readAllBytes())).join().toString();

    }


    public String uploadBigFiles(String key, InputStream inputStream) throws IOException {
        S3TransferManager transferManager = S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .uploadDirectoryMaxDepth(1).build();


        UploadRequest uploadRequest = UploadRequest.builder()
                .putObjectRequest(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .requestBody(AsyncRequestBody.fromBytes(inputStream.readAllBytes()))
                .build();

        Upload upload = transferManager.upload(uploadRequest);
        return upload.completionFuture().join().toString();

    }

    public String uploadBigFileFromStream(String key, InputStream inputStream) throws IOException {

        S3TransferManager transferManager = S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .uploadDirectoryMaxDepth(1).build();

        Path tempFile = Files.createTempFile("upload", ".tmp");
        try (inputStream) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(b -> b.bucket(bucketName).key(key))
                .source(tempFile)
                .build();

        FileUpload upload = transferManager.uploadFile(uploadFileRequest);
        upload.completionFuture().join();

        Files.deleteIfExists(tempFile); // 清理临时文件
        return "Uploaded: " + key;
    }




}