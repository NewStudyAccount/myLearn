package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticleContent;
import com.example.domain.req.SysArticleContentQueryPageReq;

public interface SysArticleContentService extends IService<SysArticleContent> {

    TableDataInfo<SysArticleContent> querySysArticleContentListPage(SysArticleContentQueryPageReq pageReq);

    SysArticleContent queryById(Long id);

    int addSysArticleContent(SysArticleContent entity);

    int updateSysArticleContentById(SysArticleContent entity);
}
