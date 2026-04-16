package com.example.domain.req.sysUser;


import com.example.domain.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserUpdateReq extends SysUser {


    private List<Long> roleIds;


}
