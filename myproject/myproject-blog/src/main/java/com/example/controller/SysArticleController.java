package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticle;
import com.example.domain.req.SysArticleQueryPageReq;
import com.example.domain.req.SysArticleReq;
import com.example.service.SysArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章表")
@RestController
@RequestMapping("/project/sysArticle")
public class SysArticleController {

    @Autowired
    private SysArticleService sysArticleService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysArticle>> list(@RequestBody SysArticleQueryPageReq pageReq) {
        TableDataInfo<SysArticle> tableDataInfo = sysArticleService.querySysArticleListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysArticle> queryById(@PathVariable("id") Long id) {
        SysArticle entity = sysArticleService.queryById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping("add")
    public Response<?> addSysArticle(@RequestBody SysArticle entity) {
        int result = sysArticleService.addSysArticle(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PostMapping("update")
    public Response<?> updateSysArticle(@RequestBody SysArticle entity) {
        int result = sysArticleService.updateSysArticleById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<?> delete(@PathVariable("id") Long id) {
        boolean result = sysArticleService.removeById(id);
        return Response.success(result);
    }
}
