package com.example.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("sys_article_content")
public class SysArticleContent {

    @TableId(value = "id")
    private Long id;

    private Long articleId;

    private String content;

    private String contentUrl;

}
