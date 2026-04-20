package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.SysMenu;
import com.example.domain.SysRoleMenu;
import com.example.domain.TableDataInfo;
import com.example.domain.req.sysMenu.SysMenuQueryPageReq;
import com.example.mapper.SysMenuMapper;
import com.example.service.SysMenuService;
import com.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
    public List<SysMenu> listRouterTree() {
        Long loginUserId = SecurityUtils.getLoginUserId();
        boolean admin = SecurityUtils.isAdmin(loginUserId);
        List<SysMenu> sysMenus = new ArrayList<>();
        if (admin){
            sysMenus = sysMenuMapper.listMenu();
        }else {
            sysMenus = sysMenuMapper.listMenuByUserId(loginUserId);
        }

        if (CollectionUtils.isEmpty(sysMenus)){
            return Collections.emptyList();
        }

        // 过滤掉按钮类型（F），只保留目录（M）和菜单（C）
        List<SysMenu> menuList = sysMenus.stream()
                .filter(item -> !"F".equals(item.getMenuType()))
                .filter(item-> item.getStatus() == 0)
                .toList();

        // 构建树形结构
        return buildMenuTree(menuList);
    }


    @Override
    public List<SysMenu> listMenuTree() {
        Long loginUserId = SecurityUtils.getLoginUserId();
        boolean admin = SecurityUtils.isAdmin(loginUserId);
        List<SysMenu> sysMenus = new ArrayList<>();
        if (admin){
            sysMenus = sysMenuMapper.listMenu();
        }else {
            sysMenus = sysMenuMapper.listMenuByUserId(loginUserId);
        }

        if (CollectionUtils.isEmpty(sysMenus)){
            return Collections.emptyList();
        }

        // 过滤掉按钮类型（F），只保留目录（M）和菜单（C）
        List<SysMenu> menuList = sysMenus.stream()
                .filter(item -> !"F".equals(item.getMenuType()))
                .toList();

        // 构建树形结构
        return buildMenuTree(menuList);
    }



    @Override
    public List<SysMenu> listMenu() {

        List<SysMenu> sysMenus = sysMenuMapper.listMenu();

        // 过滤掉按钮类型（F），只保留目录（M）和菜单（C）
        List<SysMenu> menuList = sysMenus.stream()
                .filter(item -> !"F".equals(item.getMenuType()))
                .toList();

        // 构建树形结构
        return menuList;
    }


    @Override
    public TableDataInfo<SysMenu> querySysMenuListPage(SysMenuQueryPageReq pageReq) {
        Page<SysMenu> sysMenuPage = sysMenuMapper.selectPage(pageReq.getPageQuery().build(), null);
        return TableDataInfo.build(sysMenuPage);
    }

    /**
     * 构建菜单树形结构
     * @param menus 所有菜单列表
     * @return 树形结构菜单列表
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        // 初始化每个节点的 children 为空列表
        menus.forEach(menu -> menu.setChildren(new ArrayList<>()));

        // 使用 Map 存储，方便快速查找
        Map<Integer, SysMenu> menuMap = new HashMap<>();
        menus.forEach(menu -> menuMap.put(menu.getMenuId(), menu));

        List<SysMenu> treeNodes = new ArrayList<>();

        for (SysMenu menu : menus) {
            Integer parentId = menu.getParentId();

            // 如果父节点存在且在列表中，将当前节点添加到父节点的 children 中
            if (parentId != null && menuMap.containsKey(parentId)) {
                SysMenu parentMenu = menuMap.get(parentId);
                parentMenu.getChildren().add(menu);
            } else {
                // 否则作为根节点
                treeNodes.add(menu);
            }
        }

        // 对根节点进行排序
        return treeNodes.stream()
                .sorted(Comparator.comparing(SysMenu::getMenuSort))
                .toList();
    }

    /**
     * 通过userId 提取出权限
     * @param userId
     * @return
     */
    @Override
    public List<String> listPermissionCodesByUserId(Long userId) {

        boolean admin = SecurityUtils.isAdmin(userId);
        List<String> list = sysMenuMapper.listPermissionCodesByUserId(userId);
        if (admin){
            list.add("*:*:*");
        }

        return list;
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
        String menuType = sysMenu.getMenuType();
        Integer parentId = sysMenu.getParentId();

        if (sysMenu.getMenuId() == null) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getParentId, parentId)
            .eq(SysMenu::getMenuType, menuType)
            .orderByDesc(SysMenu::getMenuId)
            .last("LIMIT 1");
            SysMenu maxMenu = sysMenuMapper.selectOne(wrapper);

            int nextId = 0;
            if (parentId == null || parentId == 0) {
                nextId = (maxMenu != null) ? maxMenu.getMenuId() + 1 : 1;
            } else {
                if (menuType.equals("C")){
                    int baseId = parentId * 1000;
                    nextId = (maxMenu != null) ? maxMenu.getMenuId() + 1 : baseId + 1;
                }else if (menuType.equals("F")){
                    int baseId = parentId * 100;
                    nextId = (maxMenu != null) ? maxMenu.getMenuId() + 1 : baseId + 1;
                }
            }

            sysMenu.setMenuId(nextId);
        }

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




