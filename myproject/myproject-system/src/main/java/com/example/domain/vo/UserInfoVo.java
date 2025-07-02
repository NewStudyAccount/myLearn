package com.example.domain.vo;


import com.example.domain.SysMenu;
import com.example.domain.SysUser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoVo {

    SysUser sysUser;

    List<String> permissionCodes;

    //用户的菜单，用于组装动态路由
    List<SysMenu> sysMenuListForDynamicRouter;
}
