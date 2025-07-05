package com.example.controller;


import com.example.aspect.ApiOperationLog;
import com.example.domain.Response;
import com.example.domain.vo.LoginUserVo;
import com.example.domain.vo.UserInfoVo;
import com.example.service.LoginService;
import com.example.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员")
@RestController
@RequestMapping("/project")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @Autowired
    private SysUserService sysUserService;


    @ApiOperationLog(description = "登录接口")
    @Operation(summary = "登录接口")
    @PostMapping("/admin/login")
    public Response<?> login(@RequestBody LoginUserVo loginUserVo){

        Response<?> login = loginService.login(loginUserVo);
        return login;
    }


    @Operation(summary = "用户登录后获取用户信息")
    @PostMapping("/queryUserInfo")
    public Response<?> getUserInfo(){
        UserInfoVo userInfoVo = sysUserService.queryUserInfoAfterLogin();
        return Response.success(userInfoVo);
    }


}
