package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/project/tools")
public class ToolsController {





    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 响应结果
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//            sysOssFileService.uploadFile(file);
            return ResponseEntity.ok("result");

    }


//    @PostMapping("/list")
//    public ResponseEntity<String> list() {
//        ListBucketsResponse listBucketsResponse = ossService.listAllBuckets();
//        return ResponseEntity.ok(listBucketsResponse.toString());
//
//    }

//    public Response<?> uploadFile(String fileName, byte[] fileBytes) {
//        return Response.success("上传成功");
//    }
//
//
//    public Response<?> downloadFile(String fileName) {
//        return Response.success("下载成功");
//    }


}
