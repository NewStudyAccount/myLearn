package com.example.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章内容
 * @TableName sys_article_content
 */
@TableName(value ="sys_article_content")
@Data
public class SysArticleContent implements Serializable {
    /**
     * 文章内容id
     */
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}