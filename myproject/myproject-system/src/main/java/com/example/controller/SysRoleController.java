package com.example.controller;


import com.example.domain.Response;
import com.example.domain.SysRole;
import com.example.domain.TableDataInfo;
import com.example.domain.req.SysRoleAddReq;
import com.example.domain.req.SysRoleQueryPageReq;
import com.example.domain.req.SysRoleUpdateReq;
import com.example.domain.vo.SysRoleVo;
import com.example.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/role")
public class SysRoleController {


    @Autowired
    private SysRoleService roleService;

    @PostMapping("/queryPage")
    public TableDataInfo<?> queryRoleListPage(@RequestBody SysRoleQueryPageReq sysRoleQueryPageReq) {
        TableDataInfo<SysRole> sysRoleTableDataInfo = roleService.queryRoleListPage(sysRoleQueryPageReq);
        return sysRoleTableDataInfo;
    }


    @PostMapping("/add")
    public Response<?> insertRole(@RequestBody @Validated SysRoleAddReq sysRoleAddReq) {
        roleService.addRole(sysRoleAddReq);
        return Response.success("新增成功");
    }

    @PostMapping("/update")
    public Response<?> updateRole(@RequestBody @Validated SysRoleUpdateReq sysRoleUpdateReq) {
        roleService.updateRole(sysRoleUpdateReq);
        return Response.success("更新成功");
    }


    @GetMapping("/query/{roleId}")
    public Response<?> queryRoleListById(@PathVariable("roleId")Long roleId) {
        SysRoleVo sysRoleVo = roleService.queryByRoleId(roleId);
        return Response.success(sysRoleVo);
    }


    public Response<?> queryRoleList() {
        return Response.success("查询成功");
    }
}
