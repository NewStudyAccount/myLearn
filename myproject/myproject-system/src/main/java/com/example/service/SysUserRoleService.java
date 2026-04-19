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

    public void addUserRoles(Long userId,List<Long> roleIds);

    void deleteUserRole(Long roleIds);


    void updateUserRole(Long userId,List<Long> roleIds);



}
