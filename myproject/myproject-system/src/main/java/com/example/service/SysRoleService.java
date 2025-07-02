package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysRole;
import com.example.domain.TableDataInfo;
import com.example.domain.req.SysRoleAddReq;
import com.example.domain.req.SysRoleQueryPageReq;
import com.example.domain.req.SysRoleUpdateReq;
import com.example.domain.vo.SysRoleVo;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2025-03-31 00:18:56
*/
public interface SysRoleService extends IService<SysRole> {



    TableDataInfo<SysRole> queryRoleListPage(SysRoleQueryPageReq sysRoleQueryPageReq);

    void queryRoleList();


    SysRoleVo queryByRoleId(Long roleId);

    List<SysRoleVo> listRoleByUserId(Long userId);

    int addRole(SysRoleAddReq sysRole);

    void updateRole(SysRoleUpdateReq sysRole);

    int deleteRole(Long roleId);



}
