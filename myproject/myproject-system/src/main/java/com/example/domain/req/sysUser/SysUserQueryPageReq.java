package com.example.domain.req.sysUser;

import com.example.domain.PageQuery;
import lombok.Data;


@Data
public class SysUserQueryPageReq {

    /**
     * 用户名
     */
    private String userName;

    private PageQuery pageQuery;

}
