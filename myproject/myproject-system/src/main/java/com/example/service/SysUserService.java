package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysUser;
import com.example.domain.TableDataInfo;
import com.example.domain.req.sysUser.SysUserAddReq;
import com.example.domain.req.sysUser.SysUserQueryPageReq;
import com.example.domain.req.sysUser.SysUserUpdateReq;
import com.example.domain.vo.SysUserVo;
import com.example.domain.vo.UserInfoVo;
import com.example.domain.req.sysUser.UserRegisterReq;

import java.util.Map;

/**
* @author QJJ
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-03-31 00:18:56
*/
public interface SysUserService extends IService<SysUser> {

    public int register(UserRegisterReq userRegisterReq);


    public SysUserVo getUserById(Long userId);

    public int addUser(SysUserAddReq sysUserAddReq);

    TableDataInfo<SysUser> queryUserListPage(SysUserQueryPageReq sysUserQueryPageReq);

//    List<String> queryUserInfoPermission(Long userId);

    Map<String,Object> getUserInfo();

    UserInfoVo queryUserInfoAfterLogin();



    void updateUserInfo(SysUserUpdateReq sysUserUpdateReq);


}
