package com.example.domain.vo;

import com.example.domain.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserVo {

    SysUser sysUser;

    private List<String> roleIds;

}
