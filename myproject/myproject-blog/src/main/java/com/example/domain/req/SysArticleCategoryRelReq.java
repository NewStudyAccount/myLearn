package com.example.domain.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName sys_article_category_rel
 */
@Data
public class SysArticleCategoryRelReq {


    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 文章id
     */
    private Long articleId;

}