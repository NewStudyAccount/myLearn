package com.example.domain.vo;


import lombok.Data;

@Data
public class DynamicRouterVo {

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 菜单名称
     */
    private String meunName;



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

}
