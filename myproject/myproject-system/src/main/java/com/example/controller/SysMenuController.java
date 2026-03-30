package com.example.controller;

import com.example.domain.Response;
import com.example.domain.SysMenu;
import com.example.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@Tag(name = "管理员")
@RestController
@RequestMapping("/project/menu")
public class SysMenuController {


    @Autowired
    private SysMenuService sysMenuService;


    @Operation(summary = "用户登录后获取动态路由信息")
    @PostMapping("/tree")
    public Response<?> getMenuTree(){
        List<SysMenu> sysMenus = sysMenuService.listMenuTree();
        return Response.success(sysMenus);
    }


}
