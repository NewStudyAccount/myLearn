package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.*;
import com.example.domain.req.sysUser.SysUserQueryPageReq;
import com.example.domain.vo.SysRoleVo;
import com.example.domain.vo.UserInfoVo;
import com.example.domain.vo.UserVo;
import com.example.mapper.SysUserMapper;
import com.example.service.*;
import com.example.utils.SecurityFrameworkUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

    /**
     * 【Security】查询用户的权限
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserPermission(Long userId) {




        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (isAdmin(loginUserId)) {
            //todo 获取所有角色
        }

        return sysMenuService.listPermissionCodesByUserId(userId);
    }



    public boolean isAdmin(Long userId){

        List<SysRoleVo> sysRoleVos = sysRoleService.listRoleByUserId(userId);
        return sysRoleVos.stream().anyMatch(sysRoleVo -> sysRoleVo.getRoleName().equals("admin"));

    }




    @Override
    public int register(UserVo userVo) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userVo.getUserName());
        sysUser.setUserPwd(new BCryptPasswordEncoder().encode(userVo.getPassword()));
        sysUser.setUserSex(userVo.getSex());

        return sysUserMapper.insert(sysUser);
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
     * 用户登录之后返回的用户信息
     * 用户信息
     * 权限信息
     * 等
     * @param userId
     */
    @Override
    public UserInfoVo queryUserInfo(Long userId) {

        SysUser sysUser = sysUserMapper.selectById(userId);
        sysUser.setUserPwd("******");

        List<String> userPermission = getUserPermission(userId);



        UserInfoVo userInfoVo = UserInfoVo.builder()
                .sysUser(sysUser).permissionCodes(userPermission)
                .build();

        return userInfoVo;

    }

    @Override
    public List<SysMenu> queryUserDynamicRouter(Long userId) {
        List<SysMenu> sysMenus = listDynamicRouterByUserId(userId);
        return sysMenus;
    }


    public List<SysMenu> listDynamicRouterByUserId(Long userId){
        List<SysMenu> sysMenus = new ArrayList<>();
        if (isAdmin(userId)) {
            sysMenus = sysMenuService.listMenu();
        }else {
            sysMenus = sysMenuService.listMenuByUserId(userId);
        }
        if (CollectionUtils.isEmpty(sysMenus)){
            return Collections.emptyList();
        }
        return sysMenus.stream().filter(item -> "C".equals(item.getMenuType())).toList();
    }
}




