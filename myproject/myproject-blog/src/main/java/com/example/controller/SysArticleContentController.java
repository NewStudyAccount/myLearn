package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticleContent;
import com.example.domain.req.SysArticleContentQueryPageReq;
import com.example.service.SysArticleContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章内容")
@RestController
@RequestMapping("/project/sysArticleContent")
public class SysArticleContentController {

    @Autowired
    private SysArticleContentService sysArticleContentService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysArticleContent>> list(@RequestBody SysArticleContentQueryPageReq pageReq) {
        TableDataInfo<SysArticleContent> tableDataInfo = sysArticleContentService.querySysArticleContentListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysArticleContent> queryById(@PathVariable("id") Long id) {
        SysArticleContent entity = sysArticleContentService.queryById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping("add")
    public Response<?> addSysArticleContent(@RequestBody SysArticleContent entity) {
        int result = sysArticleContentService.addSysArticleContent(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PostMapping("update")
    public Response<?> updateSysArticleContent(@RequestBody SysArticleContent entity) {
        int result = sysArticleContentService.updateSysArticleContentById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<?> delete(@PathVariable("id") Long id) {
        boolean result = sysArticleContentService.removeById(id);
        return Response.success(result);
    }
}
