package com.example.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.domain.SysRole;
import com.example.domain.req.SysRoleQueryPageReq;
import com.example.domain.req.SysRoleUpdateReq;
import com.example.domain.vo.SysRoleVo;
import com.example.service.SysRoleService;
import com.example.utils.SnowflakeIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色表")
@RestController
@RequestMapping("/project/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysRole>> list(@RequestBody SysRoleQueryPageReq pageReq) {
        TableDataInfo<SysRole> tableDataInfo = sysRoleService.queryRoleListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysRoleVo> getById(@PathVariable("id") Long id) {
        SysRoleVo sysRoleVo = sysRoleService.queryByRoleId(id);
        return Response.success(sysRoleVo);
    }

    @Operation(summary = "新增")
    @PostMapping
    public Response<Boolean> save(@RequestBody SysRole entity) {
        long systemNextId = SnowflakeIdUtil.systemNextId();
        entity.setRoleId(systemNextId);
        boolean result = sysRoleService.save(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PostMapping("/updateRole")
    public Response<Boolean> update(@RequestBody SysRoleUpdateReq sysRoleUpdateReq) {
        sysRoleService.updateRole(sysRoleUpdateReq);
        return Response.success(null);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        boolean result = sysRoleService.removeById(id);
        return Response.success(result);
    }
}
