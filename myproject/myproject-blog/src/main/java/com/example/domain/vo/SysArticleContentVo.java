package com.example.domain.vo;

import lombok.Data;

/**
 * 文章内容
 * @TableName sys_article_content
 */
@Data
public class SysArticleContentVo{
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

}