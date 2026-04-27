package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysCategory;
import com.example.domain.req.SysCategoryQueryPageReq;
import com.example.service.SysCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章分类")
@RestController
@RequestMapping("/project/sysCategory")
public class SysCategoryController {

    @Autowired
    private SysCategoryService sysCategoryService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysCategory>> list(@RequestBody SysCategoryQueryPageReq pageReq) {
        TableDataInfo<SysCategory> tableDataInfo = sysCategoryService.querySysCategoryListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysCategory> queryById(@PathVariable("id") Long id) {
        SysCategory entity = sysCategoryService.queryById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping("add")
    public Response<?> addSysCategory(@RequestBody SysCategory entity) {
        int result = sysCategoryService.addSysCategory(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PostMapping("update")
    public Response<?> updateSysCategory(@RequestBody SysCategory entity) {
        int result = sysCategoryService.updateSysCategoryById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<?> delete(@PathVariable("id") Long id) {
        boolean result = sysCategoryService.removeById(id);
        return Response.success(result);
    }
}
