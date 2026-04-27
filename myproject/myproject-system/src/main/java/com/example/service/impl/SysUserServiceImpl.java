package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.*;
import com.example.domain.req.sysUser.SysUserAddReq;
import com.example.domain.req.sysUser.SysUserQueryPageReq;
import com.example.domain.req.sysUser.SysUserUpdateReq;
import com.example.domain.vo.MenuTree;
import com.example.domain.vo.SysUserVo;
import com.example.domain.vo.UserInfoVo;
import com.example.domain.req.sysUser.UserRegisterReq;
import com.example.mapper.SysUserMapper;
import com.example.service.*;
import com.example.utils.SecurityUtils;
import com.example.utils.SnowflakeIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
* @author QJJ
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-03-31 00:18:56
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService, SecurityAdminService {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Autowired
    private SysUserMapper sysUserMapper;


    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Autowired
    private SysRoleService sysRoleService;


    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Autowired
    private SysMenuService sysMenuService;


    /**
     * 【Security】使用查询用户信息
     * @param userName
     * @return
     */
    @Override
    public SysUserDto queryByUserName(String userName) {

        SysUserDto sysUserDto = new SysUserDto();

        SysUser one = this.lambdaQuery().eq(SysUser::getUserName, userName).one();
        BeanUtils.copyProperties(one,sysUserDto);

        return sysUserDto;
    }

    @Override
    public List<String> getUserPermission(Long userId) {
        return sysMenuService.listPermissionCodesByUserId(userId);
    }


    @Override
    public Map<String,Object> getUserInfo() {

        Map<String,Object> resultMap = new HashMap<>();

        Long loginUserId = SecurityUtils.getLoginUserId();
        MyUserDetails loginUser = SecurityUtils.getLoginUser();
        Set<String> permissionSet = new HashSet<>(sysMenuService.listPermissionCodesByUserId(loginUserId));
        Set<String> roleSet = sysRoleService.listRoleByUserId(loginUserId);

        resultMap.put("user",loginUser);
        resultMap.put("permissions",permissionSet);
        resultMap.put("roles",roleSet);

        return resultMap;


    }



    @Override
    public int register(UserRegisterReq userRegisterReq) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userRegisterReq.getUserName());
        sysUser.setUserPwd(new BCryptPasswordEncoder().encode(userRegisterReq.getPassword()));
        sysUser.setUserSex(userRegisterReq.getSex());

        return sysUserMapper.insert(sysUser);
    }


    @Override
    public SysUserVo getUserById(Long userId) {
        SysUserVo sysUserVo = new SysUserVo();

        SysUser sysUser = sysUserMapper.selectById(userId);
        sysUser.setUserPwd(null);
        Set<String> strings = sysRoleService.listRoleByUserId(userId);

        BeanUtils.copyProperties(sysUser,sysUserVo);

        sysUserVo.setRoleIds(strings.stream().toList());

        return sysUserVo;
    }

    @Override
    public int addUser(SysUserAddReq sysUserAddReq) {

        long systemNextId = SnowflakeIdUtil.systemNextId();

        sysUserAddReq.setUserPwd(new BCryptPasswordEncoder().encode(sysUserAddReq.getUserPwd()));
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserAddReq,sysUser);
        sysUser.setUserId(systemNextId);
        sysUserMapper.insert(sysUser);

        List<Long> roleIds = sysUserAddReq.getRoleIds();
        sysUserRoleService.addUserRoles(sysUser.getUserId(),roleIds);

        return 1;
    }


    //分页查询所有用户
    @Override
    public TableDataInfo<SysUser> queryUserListPage(SysUserQueryPageReq sysUserQueryPageReq) {

        //构建查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(sysUserQueryPageReq.getUserName()),SysUser::getUserName,sysUserQueryPageReq.getUserName());

        //分页查询
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(sysUserQueryPageReq.getPageQuery().build(), queryWrapper);
        TableDataInfo<SysUser> build = TableDataInfo.build(sysUserPage);

        return build;
    }


    /**
     * 用户登录之后返回的用户信息,用户登录之后是有自己的token信息的
     * 用户信息
     * 权限信息
     * 等
     */
    @Override
    public UserInfoVo queryUserInfoAfterLogin() {





        return null;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(SysUserUpdateReq sysUserUpdateReq) {
        Long userId = sysUserUpdateReq.getUserId();
        List<Long> roleIds = sysUserUpdateReq.getRoleIds();
        //更新用户信息
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(SysUser::getUserId,userId)
                .set(StringUtils.isNotEmpty(sysUserUpdateReq.getUserName()),SysUser::getUserName,sysUserUpdateReq.getUserName())
                .set(StringUtils.isNotEmpty(sysUserUpdateReq.getUserAvatorUrl()),SysUser::getUserAvatorUrl,sysUserUpdateReq.getUserAvatorUrl())
                .set(StringUtils.isNotEmpty(sysUserUpdateReq.getUserSex()),SysUser::getUserSex,sysUserUpdateReq.getUserSex())
                .set(StringUtils.isNotEmpty(sysUserUpdateReq.getUserPhone()),SysUser::getUserPhone,sysUserUpdateReq.getUserPhone());
        sysUserMapper.update(updateWrapper);


        //更新用户-角色信息
        sysUserRoleService.updateUserRole(userId,roleIds);

    }


    public List<MenuTree> buildTree(List<SysMenu> sysMenus){

        Map<Integer,SysMenu> rootMenuMap = new HashMap<>();
        Map<Integer,List<SysMenu>> childMenuMap = new HashMap<>();

        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getParentId() == 0){
                rootMenuMap.put(sysMenu.getMenuId(),sysMenu);
            }else {
                if (!childMenuMap.containsKey(sysMenu.getParentId())){
                    List<SysMenu> childMenuList = new ArrayList<>();
                    childMenuList.add(sysMenu);
                    childMenuMap.put(sysMenu.getParentId(),childMenuList);
                }else {
                    List<SysMenu> sysMenus1 = childMenuMap.get(sysMenu.getParentId());
                    sysMenus1.add(sysMenu);
                    childMenuMap.put(sysMenu.getParentId(),sysMenus1);
                }
            }
        }

        List<MenuTree> menuTrees = new ArrayList<>();

        rootMenuMap.forEach((key,value)->{
            MenuTree menuTree = new MenuTree();
            menuTree.setMenuId(value.getMenuId());
            menuTree.setName(value.getMenuName());
            menuTree.setSort(value.getMenuSort());
            menuTree.setPath(value.getPath());
            menuTree.setComponent(value.getComponent());

            menuTree.setChildren(constructChildTree(childMenuMap.get(key)));

            menuTrees.add(menuTree);

        });

        return menuTrees;
    }

    public List<MenuTree> constructChildTree(List<SysMenu> sysMenus){

        List<MenuTree> menuTrees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            MenuTree menuTree = new MenuTree();
            menuTree.setMenuId(sysMenu.getMenuId());
            menuTree.setName(sysMenu.getMenuName());
            menuTree.setSort(sysMenu.getMenuSort());
            menuTree.setPath(sysMenu.getPath());
            menuTree.setComponent(sysMenu.getComponent());
            menuTrees.add(menuTree);
        }
        return menuTrees;
    }



    public List<MenuTree> buildTreeMethod2(List<SysMenu> sysMenus) {
        if (CollectionUtils.isEmpty(sysMenus)) {
            return Collections.emptyList();
        }

        // 构建所有节点的映射
        Map<Integer, MenuTree> menuNodeMap = new HashMap<>();
        sysMenus.forEach(menu -> menuNodeMap.put(menu.getMenuId(), new MenuTree(menu.getMenuId(),menu.getParentId(), menu.getMenuName(), menu.getMenuSort(), menu.getPerCode(), menu.getPath(), menu.getComponent())));

        // 构建父子关系
        List<MenuTree> rootNodes = new ArrayList<>();

        sysMenus.forEach(menu -> {
            MenuTree menuTree = menuNodeMap.get(menu.getMenuId());
            Integer parentId = menu.getParentId();

            if (parentId == null || parentId == 0) {
                //父级目录的组件全部传 Layout，默认都使用主页的目录
                menuTree.setComponent("Layout");
                rootNodes.add(menuTree);
            } else {
                MenuTree parent = menuNodeMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menuTree);
                }
            }
        });

        // 排序
        Comparator<MenuTree> comparator = Comparator.comparing(MenuTree::getMenuId).thenComparing(MenuTree::getSort);
        sortTree(rootNodes, comparator);

        return rootNodes;
    }

    private void sortTree(List<MenuTree> nodes, Comparator<MenuTree> comparator) {
        if (nodes == null || nodes.isEmpty()) return;

        nodes.sort(comparator);
        nodes.forEach(node -> sortTree(node.getChildren(), comparator));
    }



}




