package com.example.controller;


import com.example.domain.Response;
import com.example.domain.pojo.SysArticleTagRel;
import com.example.domain.req.SysArticleTagRelReq;
import com.example.service.SysArticleTagRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章标签关系接口")
@RestController
@RequestMapping("/sysArticleTagRel")
public class SysArticleTagRelController {


    @Autowired
    private SysArticleTagRelService sysArticleTagRelService;

    @Operation(summary = "查询文章标签关系接口")
    @GetMapping("/queryArticleTagRelList/{articleId}")
    public Response<?> queryArticleTagRelList(@PathVariable("articleId") Long articleId) {
        return Response.success(sysArticleTagRelService.queryArticleTagRelList(articleId));
    }

    @Operation(summary = "添加文章标签关系接口")
    @PostMapping("/addArticleTagRel")
    public Response<?> addArticleTagRel(@RequestBody SysArticleTagRelReq  sysArticleTagRelReq) {
        return Response.success(sysArticleTagRelService.addArticleTagRel(sysArticleTagRelReq));
    }

}
