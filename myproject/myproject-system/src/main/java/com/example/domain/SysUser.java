package com.example.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO,value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPwd;

    /**
     * 头像
     */
    private String userAvatorUrl;

    /**
     * 性别
     */
    private String userSex;

    /**
     * 手机
     */
    private String userPhone;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改人id
     */
    private Long updateId;

    /**
     * 修改时间
     */
    private Date updateDate;

    /**
     * 逻辑删除0：有效，1删除
     */
    @TableLogic
    private String isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}