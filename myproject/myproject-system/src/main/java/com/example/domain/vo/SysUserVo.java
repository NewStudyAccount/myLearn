package com.example.domain.vo;

import com.example.domain.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserVo extends SysUser{


    private List<String> roleIds;

}
