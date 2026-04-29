package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticleContent;
import com.example.domain.req.SysArticleContentQueryPageReq;
import com.example.domain.req.SysArticleContentReq;
import com.example.domain.vo.SysArticleContentVo;

public interface SysArticleContentService extends IService<SysArticleContent> {

    TableDataInfo<SysArticleContent> querySysArticleContentListPage(SysArticleContentQueryPageReq pageReq);

    SysArticleContent queryById(Long id);

    SysArticleContentVo queryByArticleId(Long id);

    int addSysArticleContent(SysArticleContentReq sysArticleContentReq);

    int updateSysArticleContentById(SysArticleContentReq sysArticleContentReq);
}
