package com.example.domain.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.domain.PageQuery;
import lombok.Data;


@Data
public class SysRoleQueryPageReq {

    /**
     * 角色名
     */
    private String roleName;

    private PageQuery pageQuery;

}