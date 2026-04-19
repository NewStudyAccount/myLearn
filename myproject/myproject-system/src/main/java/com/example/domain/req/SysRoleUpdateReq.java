package com.example.domain.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.domain.SysRole;
import lombok.Data;

import java.util.List;


@Data
public class SysRoleUpdateReq extends SysRole {


    private List<Integer> menuIds;

}