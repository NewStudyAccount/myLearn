package com.example.controller;

import com.example.domain.Response;
import com.example.domain.SysMenu;
import com.example.domain.TableDataInfo;
import com.example.domain.req.sysMenu.SysMenuQueryPageReq;
import com.example.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Tag(name = "管理员")
@RestController
@RequestMapping("/project/sysMenu")
public class SysMenuController {


    @Autowired
    private SysMenuService sysMenuService;


    @Operation(summary = "用户登录后获取动态路由信息")
    @PostMapping("/tree")
    public Response<?> getMenuTree(){
        List<SysMenu> sysMenus = sysMenuService.listRouterTree();
        return Response.success(sysMenus);
    }


    @Operation(summary = "路由信息")
    @PostMapping("/listTree")
    public Response<?> getMenuListTree(){
        List<SysMenu> sysMenus = sysMenuService.listMenuTree();
        return Response.success(sysMenus);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysMenu>> list(@RequestBody SysMenuQueryPageReq pageReq) {
        TableDataInfo<SysMenu> tableDataInfo = sysMenuService.querySysMenuListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysMenu> getById(@PathVariable Long id) {
        SysMenu entity = sysMenuService.getById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping
    public Response<?> save(@RequestBody SysMenu entity) {
        int i = sysMenuService.addMenu(entity);
        return Response.success(i);
    }

    @Operation(summary = "修改")
    @PutMapping
    public Response<Boolean> update(@RequestBody SysMenu entity) {
        boolean result = sysMenuService.updateById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable("id") Long id) {
        boolean result = sysMenuService.removeById(id);
        return Response.success(result);
    }

}
