package com.example.controller;


import com.example.domain.Response;
import com.example.service.SysOssFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/project/oss")
public class OssController {

    @Autowired
    private SysOssFileService sysOssFileService;



    @PostMapping("/upload")
    public Response<?> uploadFile(@RequestParam("file") MultipartFile file){
        String url = sysOssFileService.uploadFile(file);
        Map<String,String> map = new HashMap<>();
        map.put("url",url);
        return Response.success(map);
    }


    @PostMapping("/uploadBigFile")
    public Response<?> uploadBigFile(@RequestParam("file") MultipartFile file){
        String url = sysOssFileService.uploadBigFile(file);
        Map<String,String> map = new HashMap<>();
        map.put("url",url);
        return Response.success(map);
    }




}
