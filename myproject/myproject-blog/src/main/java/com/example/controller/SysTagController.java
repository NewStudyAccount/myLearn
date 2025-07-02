package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysTag;
import com.example.domain.req.SysTagQueryPageReq;
import com.example.domain.req.SysTagReq;
import com.example.service.SysTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tag标签接口")
@RestController
@RequestMapping("/sysTag")
public class SysTagController {

    @Autowired
    private SysTagService sysTagService;


    @Operation(summary = "分页查询tag标签接口")
    @PostMapping("/queryTagListPage")
    public TableDataInfo<?> queryTagListPage(@RequestBody SysTagQueryPageReq sysTagQueryPageReq){
        return sysTagService.queryTagListPage(sysTagQueryPageReq);
    }

    @Operation(summary = "查询tag标签接口")
    @PostMapping("/queryTagList")
    public Response<?> queryTagList(){
        return Response.success(sysTagService.queryTagList());
    }

    @Operation(summary = "添加tag标签接口")
    @PostMapping("/addTag")
    public Response<?> addTag(@RequestBody SysTagReq sysTagReq){
        int i = sysTagService.addTag(sysTagReq);
        return Response.success(i);
    }


}
