package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysTag;
import com.example.domain.req.SysTagQueryPageReq;
import com.example.service.SysTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "标签")
@RestController
@RequestMapping("/project/sysTag")
public class SysTagController {

    @Autowired
    private SysTagService sysTagService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysTag>> list(@RequestBody SysTagQueryPageReq pageReq) {
        TableDataInfo<SysTag> tableDataInfo = sysTagService.querySysTagListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysTag> queryById(@PathVariable("id") Long id) {
        SysTag entity = sysTagService.queryById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping("add")
    public Response<?> addSysTag(@RequestBody SysTag entity) {
        int result = sysTagService.addSysTag(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PostMapping("update")
    public Response<?> updateSysTag(@RequestBody SysTag entity) {
        int result = sysTagService.updateSysTagById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<?> delete(@PathVariable("id") Long id) {
        boolean result = sysTagService.removeById(id);
        return Response.success(result);
    }
}
