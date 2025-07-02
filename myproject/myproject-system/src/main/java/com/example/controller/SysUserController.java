package com.example.controller;


import com.example.domain.Response;
import com.example.domain.SysMenu;
import com.example.domain.req.sysUser.SysUserQueryReq;
import com.example.domain.vo.UserInfoVo;
import com.example.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "用户信息")
@RestController
@RequestMapping("/project/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    @Operation(summary = "用户登陆后查询用户信息")
    @PostMapping("/queryUserInfo")
    public Response<?> queryUserInfo(@RequestBody SysUserQueryReq sysUserQueryReq){
        UserInfoVo userInfoVo = sysUserService.queryUserInfo(sysUserQueryReq.getUserId());
        return Response.success(userInfoVo);
    }

    @Operation(summary = "查询用户动态路由信息")
    @PostMapping("/queryUserDynamicRouter")
    public Response<?> queryUserDynamicRouter(@RequestBody SysUserQueryReq sysUserQueryReq){
        List<SysMenu> sysMenus = sysUserService.queryUserDynamicRouter(sysUserQueryReq.getUserId());
        return Response.success(sysMenus);
    }


}
