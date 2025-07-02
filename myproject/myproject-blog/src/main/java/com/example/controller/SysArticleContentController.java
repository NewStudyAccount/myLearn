package com.example.controller;

import com.example.domain.Response;
import com.example.domain.pojo.SysArticleContent;
import com.example.domain.req.SysArticleContentReq;
import com.example.domain.vo.SysArticleContentVo;
import com.example.service.SysArticleContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章内容接口")
@RestController
@RequestMapping("/article/content")
public class SysArticleContentController {

    @Autowired
    private SysArticleContentService sysArticleContentService;


    @Operation(summary = "查询文章内容接口")
    @GetMapping("/queryById/{articleId}")
    public Response queryArticleContentWithConvert(@PathVariable("articleId") Long articleId) {

        SysArticleContentVo articleContentWithConvert = sysArticleContentService.getArticleContentWithConvert(articleId);

        return Response.success(articleContentWithConvert);
    }

    @Operation(summary = "保存文章内容接口")
    @PostMapping("/save")
    public Response saveArticleContent(@RequestBody SysArticleContentReq sysArticleContentReq) {

        int result = sysArticleContentService.saveArticleContent(sysArticleContentReq);

        return Response.success(result);
    }


}
