package com.example.controller;


import com.example.domain.Response;
import com.example.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
