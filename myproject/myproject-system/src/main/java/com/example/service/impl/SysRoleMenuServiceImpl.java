package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.SysRoleMenu;
import com.example.mapper.SysRoleMenuMapper;
import com.example.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author AI
* @description 针对表【sys_role_menu(角色-菜单权限表)】的数据库操作Service实现
* @createDate 2025-05-27 10:49:32
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService {

    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;


    @Override
    public List<SysRoleMenu> listRoleMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleMenu::getRoleId,roleId);

        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(lambdaQueryWrapper);

        return sysRoleMenus;
    }

    @Override
    public void addRoleMenu(Long roleId, List<Integer> menuIds) {

        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        for (Integer menuId : menuIds) {
            sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMeunId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        baseMapper.insert(sysRoleMenuList);

    }

    @Override
    public void deleteRoleMenu(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleMenu::getRoleId,roleId);
        baseMapper.delete(lambdaQueryWrapper);

    }

    /**
     * 更新role-menu 先删后增
     * @param roleId
     * @param menuIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long roleId, List<Integer> menuIds) {
        deleteRoleMenu(roleId);
        addRoleMenu(roleId, menuIds);

    }
}




