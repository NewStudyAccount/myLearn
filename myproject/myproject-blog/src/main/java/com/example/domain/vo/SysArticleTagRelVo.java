package com.example.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章标签关系表
 * @TableName sys_article_tag_rel
 */
@Data
public class SysArticleTagRelVo implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

    private String tagName;

}