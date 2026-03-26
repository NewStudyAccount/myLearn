package com.example.controller;


import com.example.aspect.ApiOperationLog;
import com.example.domain.Response;
import com.example.domain.vo.LoginUserVo;
import com.example.service.AuthService;
import com.example.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "管理员")
@RestController
@RequestMapping("/project/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @Autowired
    private SysUserService sysUserService;


    @ApiOperationLog(description = "登录接口")
    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Response<?> login(@RequestBody LoginUserVo loginUserVo){

        Response<?> login = authService.login(loginUserVo);
        return login;
    }


    @Operation(summary = "获取用户信息")
    @PostMapping("/me")
    public Response<?> getUserInfo(){
        Map<String, Object> userInfo = sysUserService.getUserInfo();
        return Response.success(userInfo);
    }





}
