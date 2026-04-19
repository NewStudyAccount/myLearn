package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.SysUserRole;
import com.example.domain.req.sysUserRole.SysUserRoleAddReq;
import com.example.mapper.SysUserRoleMapper;
import com.example.service.SysUserRoleService;
import com.example.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2025-03-31 00:18:56
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;



    @Override
    public void queryUserRolePage() {

    }

    @Override
    public List<SysUserRole> queryUserRoleList(Long userId) {
        return this.lambdaQuery().eq(SysUserRole::getUserId,userId).list();

    }



    @Override
    public void addUserRoles(Long userId, List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        SysUserRole sysUserRole = new SysUserRole();
        for (Long roleId : roleIds) {
            sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            sysUserRoleList.add(sysUserRole);
        }

        baseMapper.insert(sysUserRoleList);
    }

    /**
     * 删除用户-角色关系
     * @param userId
     */
    @Override
    public void deleteUserRole(Long userId) {

        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserRole::getUserId,userId);

        baseMapper.delete(lambdaQueryWrapper);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(Long userId,List<Long> roleIds) {
        deleteUserRole(userId);

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        SysUserRole sysUserRole = new SysUserRole();
        for (Long roleId : roleIds) {
            sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            sysUserRoleList.add(sysUserRole);
        }
        //新增用户-角色关系
        baseMapper.insert(sysUserRoleList);

    }
}




