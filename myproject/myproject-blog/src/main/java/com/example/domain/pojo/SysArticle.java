package com.example.domain.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章表
 * @TableName sys_article
 */
@TableName(value ="sys_article")
@Data
public class SysArticle implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章名
     */
    private String title;

    /**
     * 预览图
     */
    private String cover;

    /**
     * 删除标志
     */
    private String isDeleted;

    /**
     * 阅读次数
     */
    private Integer readNum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}