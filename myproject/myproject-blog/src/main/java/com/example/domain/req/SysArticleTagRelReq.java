package com.example.domain.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章标签关系表
 * @TableName sys_article_tag_rel
 */
@Data
public class SysArticleTagRelReq{


    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

}