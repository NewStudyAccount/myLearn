package com.example.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuTree {

    private Integer menuId;
    private Integer parentId;
    private String name;
    private Integer sort;
    private String perCode;
    private String path;
    private String component;

    private List<MenuTree> children = new ArrayList<>();

    public MenuTree(Integer menuId,Integer parentId,String name, Integer sort, String perCode, String path, String component) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.name = name;
        this.sort = sort;
        this.perCode = perCode;
        this.path = path;
        this.component = component;
    }
}
