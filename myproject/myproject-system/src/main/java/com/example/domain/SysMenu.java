package com.example.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 菜单表
 * @TableName sys_menu
 */
@Data
public class SysMenu implements Serializable {
    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Integer menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 权限code
     */
    private String perCode;

    /**
     * 菜单类型 （M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 排序
     */
    private Integer menuSort;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 
     */
    private String componentName;

    private static final long serialVersionUID = 1L;
}