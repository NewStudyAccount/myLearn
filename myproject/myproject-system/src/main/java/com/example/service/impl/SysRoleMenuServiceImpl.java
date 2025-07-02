package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.SysRoleMenu;
import com.example.mapper.SysRoleMenuMapper;
import com.example.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}




