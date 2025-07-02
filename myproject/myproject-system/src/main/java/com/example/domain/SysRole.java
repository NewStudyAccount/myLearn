package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色表
 * @TableName sys_role
 */
@TableName(value ="sys_role")
@Data
public class SysRole implements Serializable {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO,value = "role_id")
    private Long roleId;

    /**
     * 角色名
     */
    @TableField(value = "role_name")
    private String roleName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}