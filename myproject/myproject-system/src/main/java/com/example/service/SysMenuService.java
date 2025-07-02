package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysMenu;
import com.example.domain.SysRoleMenu;

import java.util.List;

/**
* @author AI
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2025-05-27 10:49:32
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> listMenu();

    List<SysMenu> listMenuByUserId(Long userId);

    List<String> listPermissionCodesByUserId(Long userId);

    List<SysMenu> listMenuByRoleMenu(List<SysRoleMenu> sysRoleMenuList);

    SysMenu queryMenuByMenuId(Long MenuId);

    int addMenu(SysMenu sysMenu);

    int updateMenu(SysMenu sysMenu);

    int deleteMenu(Long menuId);






}
