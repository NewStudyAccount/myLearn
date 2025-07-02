package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.SysMenu;
import com.example.domain.SysRoleMenu;
import com.example.mapper.SysMenuMapper;
import com.example.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author AI
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2025-05-27 10:49:32
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Override
    public List<SysMenu> listMenu() {
        return sysMenuMapper.listMenu();
    }

    /**
     * 通过userId 关系表查询所有的菜单信息。动态路由信息
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> listMenuByUserId(Long userId) {
        return sysMenuMapper.listMenuByUserId(userId);
    }

    /**
     * 通过userId 提取出权限
     * @param userId
     * @return
     */
    @Override
    public List<String> listPermissionCodesByUserId(Long userId) {
        return sysMenuMapper.listPermissionCodesByUserId(userId);
    }

    /**
     * 通过role_menu 关系表查询所有的菜单信息。后续从菜单信息中提取出权限、动态路由信息
     * @param sysRoleMenuList
     * @return
     */

    @Override
    public List<SysMenu> listMenuByRoleMenu(List<SysRoleMenu> sysRoleMenuList) {

//        if (CollectionUtils.isEmpty(sysRoleMenuList)){
//            return new ArrayList<>();
//        }
//
//
//        List<CompletableFuture<SysMenu>> futures = new ArrayList<>();
//
//        for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
//            Long meunId = sysRoleMenu.getMeunId();
//            CompletableFuture<SysMenu> completableFuture = CompletableFuture.supplyAsync(() -> {
//                return queryMenuByMenuId(meunId);
//            },threadPoolTaskExecutor);
//
//            futures.add(completableFuture);
//        }
//
//        List<SysMenu> list = futures.stream()
//                .map(CompletableFuture::join)
//                .filter(Objects::nonNull)
//                .sorted(Comparator.comparingInt(SysMenu::getSort)) // 按 sort 升序排序
//                .toList();
//
//        return list;
        return new ArrayList<>();
    }

    @Override
    public SysMenu queryMenuByMenuId(Long MenuId) {

        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysMenu::getParentId,MenuId);

        return sysMenuMapper.selectOne(lambdaQueryWrapper);

    }

    @Override
    public int addMenu(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu);
    }

    @Override
    public int updateMenu(SysMenu sysMenu) {
        return sysMenuMapper.updateById(sysMenu);
    }

    @Override
    public int deleteMenu(Long menuId) {
        //删除暂不实现
        return 0;
    }
}




