package com.example.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 标签
 * @TableName sys_tag
 */
@TableName(value ="sys_tag")
@Data
public class SysTag implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 标签名
     */
    private String name;

    /**
     * 
     */
    private String idDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}