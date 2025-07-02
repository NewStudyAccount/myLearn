package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysUserRole;
import com.example.domain.req.sysUserRole.SysUserRoleAddReq;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service
* @createDate 2025-03-31 00:18:56
*/
public interface SysUserRoleService extends IService<SysUserRole> {

    void queryUserRolePage();

    List<SysUserRole> queryUserRoleList(Long userId);

    void addUserRole(SysUserRoleAddReq sysUserRoleAddReq);

    void deleteUserRole(Long id);



}
