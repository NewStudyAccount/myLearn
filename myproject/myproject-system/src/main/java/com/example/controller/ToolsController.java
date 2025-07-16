package com.example.controller;


import com.example.domain.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/tools")
public class ToolsController {


    public Response<?> uploadFile(String fileName, byte[] fileBytes) {
        return Response.success("上传成功");
    }


    public Response<?> downloadFile(String fileName) {
        return Response.success("下载成功");
    }


}
