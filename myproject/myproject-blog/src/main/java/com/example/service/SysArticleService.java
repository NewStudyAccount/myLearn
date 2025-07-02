package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.pojo.SysArticle;
import com.example.domain.req.SysArticleReq;

/**
* @author QJJ
* @description 针对表【sys_article(文章表)】的数据库操作Service
* @createDate 2025-04-01 23:31:09
*/
public interface SysArticleService extends IService<SysArticle> {

    public int saveArticle(SysArticleReq sysArticleReq);

    public int deleteArticle(Long articleId);
}
