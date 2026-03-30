package com.example.es.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单表
 */
@Data
public class SysMenu implements Serializable {
    /**
     * 菜单id
     */
    @JsonProperty("menu_id")
    private Integer menuId;

    /**
     * 菜单名称
     */
    @JsonProperty("menu_name")
    private String menuName;

    /**
     * 权限code
     */
    @JsonProperty("per_code")
    private String perCode;

    /**
     * 菜单类型 （M目录 C菜单 F按钮）
     */
    @JsonProperty("menu_type")
    private String menuType;

    /**
     * 排序
     */
    @JsonProperty("menu_sort")
    private Integer menuSort;

    /**
     * 父级id
     */
    @JsonProperty("parent_id")
    private Integer parentId;

    /**
     * 路由地址
     */
    @JsonProperty("path")
    private String path;

    /**
     * 组件路径
     */
    @JsonProperty("component")
    private String component;

    /**
     * 
     */
    @JsonProperty("component_name")
    private String componentName;

    private static final long serialVersionUID = 1L;
}