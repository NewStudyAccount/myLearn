package com.example.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName sys_article_category_rel
 */
@TableName(value ="sys_article_category_rel")
@Data
public class SysArticleCategoryRel implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 文章id
     */
    private Long articleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}