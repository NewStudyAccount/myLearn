package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.pojo.SysArticleTagRel;
import com.example.domain.req.SysArticleTagRelReq;
import com.example.domain.vo.SysArticleTagRelVo;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_article_tag_rel(文章标签关系表)】的数据库操作Service
* @createDate 2025-04-01 23:31:09
*/
public interface SysArticleTagRelService extends IService<SysArticleTagRel> {

    List<SysArticleTagRel> queryArticleTagRelList(Long articleId);

    int addArticleTagRel(SysArticleTagRelReq sysArticleTagRelReq);

    int deleteArticleTagRel(Long id);

}
