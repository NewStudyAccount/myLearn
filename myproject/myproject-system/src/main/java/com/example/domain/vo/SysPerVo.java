package com.example.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 缺陷表
 * @TableName sys_per
 */
@Data
public class SysPerVo {
    /**
     * 权限id
     */
    private Long perId;

    /**
     * 权限code
     */
    private String perCode;

}