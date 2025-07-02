package com.example.domain.req;


import lombok.Data;

@Data
public class SysArticleContentReq {


    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;

}
