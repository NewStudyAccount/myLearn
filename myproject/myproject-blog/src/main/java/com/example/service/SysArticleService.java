package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticle;
import com.example.domain.req.SysArticleQueryPageReq;
import com.example.domain.req.SysArticleReq;

public interface SysArticleService extends IService<SysArticle> {

    TableDataInfo<SysArticle> querySysArticleListPage(SysArticleQueryPageReq pageReq);

    SysArticle queryById(Long id);

    int addSysArticle(SysArticle entity);

    int updateSysArticleById(SysArticle entity);
}
