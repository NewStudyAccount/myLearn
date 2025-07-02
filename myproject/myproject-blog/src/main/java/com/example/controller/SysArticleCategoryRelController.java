package com.example.controller;


import com.example.domain.Response;
import com.example.domain.pojo.SysArticleCategoryRel;
import com.example.domain.req.SysArticleCategoryRelReq;
import com.example.service.SysArticleCategoryRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "文章分类关系接口")
@RestController
@RequestMapping("/sysArticleCategoryRel")
public class SysArticleCategoryRelController {


    @Autowired
    private SysArticleCategoryRelService sysArticleCategoryRelService;

    @Operation(summary = "查询文章分类关系接口")
    @GetMapping("/queryArticleCategoryRelList/{articleId}")
    public Response<?> queryArticleCategoryRelList(@PathVariable("articleId") Long articleId) {
        List<SysArticleCategoryRel> sysArticleCategoryRels = sysArticleCategoryRelService.queryArticleCategoryRelList(articleId);
        return Response.success(sysArticleCategoryRels);
    }

    @Operation(summary = "添加文章分类关系接口")
    @PostMapping("/addArticleCategoryRel")
    public Response<?> addArticleCategoryRel(@RequestBody SysArticleCategoryRelReq sysArticleCategoryRelReq) {
        int result = sysArticleCategoryRelService.addArticleCategoryRel(sysArticleCategoryRelReq);
        return Response.success(result);
    }




}
