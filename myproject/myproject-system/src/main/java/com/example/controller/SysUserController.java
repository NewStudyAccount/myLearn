package com.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.Response;
import com.example.domain.SysUser;
import com.example.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "用户信息")
@RestController
@RequestMapping("/project/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;



    @Operation(summary = "获取用户信息")
    @PostMapping("/me")
    public Response<?> getUserInfo(){
        Map<String, Object> userInfo = sysUserService.getUserInfo();
        return Response.success(userInfo);
    }



    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Response<Page<SysUser>> list(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        Page<SysUser> page = sysUserService.page(new Page<>(pageNum, pageSize));
        return Response.success(page);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysUser> getById(@PathVariable Long id) {
        SysUser entity = sysUserService.getById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping
    public Response<Boolean> save(@RequestBody SysUser entity) {
        boolean result = sysUserService.save(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PutMapping
    public Response<Boolean> update(@RequestBody SysUser entity) {
        boolean result = sysUserService.updateById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        boolean result = sysUserService.removeById(id);
        return Response.success(result);
    }

}
