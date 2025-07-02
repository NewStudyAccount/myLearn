package com.example.domain.req.sysUserRole;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关联表
 * @TableName sys_user_role
 */
@Data
public class SysUserRoleAddReq{
    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long roleId;
}