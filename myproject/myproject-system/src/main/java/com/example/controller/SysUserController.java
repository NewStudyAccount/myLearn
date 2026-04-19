package com.example.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.*;
import com.example.domain.req.sysUser.SysUserAddReq;
import com.example.domain.req.sysUser.SysUserQueryPageReq;
import com.example.domain.req.sysUser.SysUserUpdateReq;
import com.example.domain.vo.SysUserVo;
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
    @PostMapping("/list")
    public Response<?> list(@RequestBody SysUserQueryPageReq pageReq) {
        TableDataInfo<SysUser> sysUserTableDataInfo = sysUserService.queryUserListPage(pageReq);
        return Response.success(sysUserTableDataInfo);


    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysUserVo> getById(@PathVariable("id") Long id) {
        SysUserVo userById = sysUserService.getUserById(id);
        return Response.success(userById);
    }

    @Operation(summary = "新增")
    @PostMapping
    public Response<?> save(@RequestBody SysUserAddReq entity) {
        int i = sysUserService.addUser(entity);
        return Response.success(i);
    }

    @Operation(summary = "修改")
    @PutMapping
    public Response<?> update(@RequestBody SysUserUpdateReq sysUserUpdateReq) {
        sysUserService.updateUserInfo(sysUserUpdateReq);
        return Response.success(1);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable("id") Long id) {
        //todo 后续可以增加补全  删除用户、用户-角色 信息
        boolean result = sysUserService.removeById(id);
        return Response.success(result);
    }

}
