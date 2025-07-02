package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysRoleMenu;

import java.util.List;

/**
* @author AI
* @description 针对表【sys_role_menu(角色-菜单权限表)】的数据库操作Service
* @createDate 2025-05-27 10:49:32
*/
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    List<SysRoleMenu> listRoleMenuByRoleId(Long roleId);

}
