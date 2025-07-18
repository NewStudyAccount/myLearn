package com.example.controller;


import com.example.config.OssService;
import com.example.domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/project/tools")
public class ToolsController {



    @Autowired
    private OssService ossService;

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 响应结果
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            String result = ossService.uploadFile("test2/"+originalFilename, inputStream);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }

    @PostMapping("/list")
    public ResponseEntity<String> list() {
        ListBucketsResponse listBucketsResponse = ossService.listAllBuckets();
        return ResponseEntity.ok(listBucketsResponse.toString());

    }

    public Response<?> uploadFile(String fileName, byte[] fileBytes) {
        return Response.success("上传成功");
    }


    public Response<?> downloadFile(String fileName) {
        return Response.success("下载成功");
    }


}
