package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.*;
import com.example.domain.req.SysRoleAddReq;
import com.example.domain.req.SysRoleQueryPageReq;
import com.example.domain.req.SysRoleUpdateReq;
import com.example.domain.req.sysUser.SysUserQueryPageReq;
import com.example.domain.vo.SysRoleVo;
import com.example.mapper.SysRoleMapper;
import com.example.service.SysMenuService;
import com.example.service.SysRoleMenuService;
import com.example.service.SysRoleService;
import com.example.service.SysUserRoleService;
import com.example.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author QJJ
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2025-03-31 00:18:56
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public TableDataInfo<SysRole> queryRoleListPage(SysRoleQueryPageReq sysRoleQueryPageReq) {

        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(sysRoleQueryPageReq.getPageQuery().build(), null);
        TableDataInfo<SysRole> build = TableDataInfo.build(sysRolePage);
        return build;


    }



    @Override
    public SysRoleVo queryByRoleId(Long roleId) {
        SysRoleVo sysRoleVo = new SysRoleVo();
        SysRole sysRole = this.lambdaQuery().eq(SysRole::getRoleId, roleId).one();

        BeanUtils.copyProperties(sysRole, sysRoleVo);

        if (sysRole.getRoleId() == 1L){
            List<SysMenu> sysMenus = sysMenuService.listMenu();
            if (!CollectionUtils.isEmpty(sysMenus)){
                sysRoleVo.setMenuIds(sysMenus.stream()
                        .distinct()
                        .map(SysMenu::getMenuId)
                        .toList());
            }
        }else {
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.listRoleMenuByRoleId(roleId);
            if (!CollectionUtils.isEmpty(sysRoleMenus)){
                List<Integer> collect = sysRoleMenus.stream()
                        .distinct()
                        .map(SysRoleMenu::getMeunId)
                        .toList();
                sysRoleVo.setMenuIds(collect);
            }
        }

        return sysRoleVo;
    }

    @Override
    public Set<String> listRoleByUserId(Long userId) {

        Set<String> result = new HashSet<>();

        List<SysUserRole> sysUserRoles = sysUserRoleService.queryUserRoleList(userId);
        if (CollectionUtils.isEmpty(sysUserRoles)) {
            return result;
        }
        for (SysUserRole sysUserRole : sysUserRoles) {
            Long roleId = sysUserRole.getRoleId();
            SysRole sysRole = this.lambdaQuery().eq(SysRole::getRoleId, roleId).one();
            result.add(sysRole.getRoleName());
        }

        return result;
    }

    @Override
    public int addRole(SysRoleAddReq sysRoleAddReq) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleAddReq, sysRole);
        return this.baseMapper.insert(sysRole);
    }

    /**
     * 先更新角色，更新角色-菜单
     * @param sysRoleUpdateReq
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleUpdateReq sysRoleUpdateReq) {
        this.lambdaUpdate()
                .set(!StringUtils.isEmpty(sysRoleUpdateReq.getRoleName()),SysRole::getRoleName, sysRoleUpdateReq.getRoleName())  // 设置待更新字段值
                .eq(SysRole::getRoleId, sysRoleUpdateReq.getRoleId())       // WHERE 条件：role_id = 参数值
                .update();                                         // 执行更新

        //更新角色-菜单
        sysRoleMenuService.updateRoleMenu(sysRoleUpdateReq.getRoleId(), sysRoleUpdateReq.getMenuIds());


    }

    @Override
    public int deleteRole(List<Long> roleIds) {
        Long loginUserId = SecurityUtils.getLoginUserId();
        return 0;
    }
}




