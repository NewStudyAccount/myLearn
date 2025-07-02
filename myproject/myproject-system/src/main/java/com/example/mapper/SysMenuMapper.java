package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author AI
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2025-05-27 10:47:08
* @Entity generator.domain.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> listMenu();
    List<SysMenu> listMenuByUserId(@Param("userId") Long userId);

    List<String> listPermissionCodesByUserId(@Param("userId") Long userId);

}




