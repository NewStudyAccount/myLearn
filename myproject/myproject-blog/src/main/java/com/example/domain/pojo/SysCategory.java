package com.example.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import lombok.Data;

/**
 * 文章分类
 * @TableName sys_category
 */
@TableName(value ="sys_category")
@Data
public class SysCategory implements Serializable {
    /**
     * 分类id
     */
    // 后端解决方案：Jackson 序列化时转为 String
//    @JsonSerialize(using = NumberSerializer.class)
    private Long id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 删除标志
     */
    private String isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}