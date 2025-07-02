package com.example.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 角色-菜单权限表
 * @TableName sys_role_menu
 */
@Data
public class SysRoleMenu implements Serializable {


    /**
     * 
     */
    private Long roleId;

    /**
     * 
     */
    private Integer meunId;

    private static final long serialVersionUID = 1L;
}