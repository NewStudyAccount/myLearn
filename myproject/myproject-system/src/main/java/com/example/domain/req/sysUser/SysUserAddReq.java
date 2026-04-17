package com.example.domain.req.sysUser;


import com.example.domain.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserAddReq extends SysUser {


    private List<Long> roleIds;


}
