package com.example.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.domain.SysRole;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 * @TableName sys_role
 */
@Data
public class SysRoleVo  extends SysRole{

    private List<Integer> menuIds;

}