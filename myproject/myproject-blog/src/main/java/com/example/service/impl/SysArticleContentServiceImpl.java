package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticle;
import com.example.domain.pojo.SysArticleContent;
import com.example.domain.req.SysArticleContentQueryPageReq;
import com.example.domain.req.SysArticleContentReq;
import com.example.domain.vo.SysArticleContentHtmlVo;
import com.example.domain.vo.SysArticleContentVo;
import com.example.mapper.SysArticleContentMapper;
import com.example.oss.domain.SysOssFile;
import com.example.oss.service.OssFileService;
import com.example.service.SysArticleContentService;
import com.example.service.SysArticleService;
import com.example.utils.MarkDownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
public class SysArticleContentServiceImpl extends ServiceImpl<SysArticleContentMapper, SysArticleContent> implements SysArticleContentService {


    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private SysArticleService sysArticleService;

    @Override
    public TableDataInfo<SysArticleContent> querySysArticleContentListPage(SysArticleContentQueryPageReq pageReq) {
        Page<SysArticleContent> page = baseMapper.selectPage(pageReq.getPageQuery().build(), null);
        return TableDataInfo.build(page);
    }


    @Override
    public SysArticleContentVo queryByArticleId(Long id) {
        SysArticleContentVo entity = new SysArticleContentVo();
        LambdaQueryWrapper<SysArticleContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysArticleContent::getArticleId, id);
        SysArticleContent sysArticleContent = baseMapper.selectOne(lambdaQueryWrapper);

        // 将 sysArticleContent 转为 sysArticleContentVo
        BeanUtils.copyProperties(sysArticleContent, entity);

        // 下载文件
        Long ossId = sysArticleContent.getOssId();
        SysOssFile sysOssFile = ossFileService.queryById(ossId);
        byte[] contentBytes = ossFileService.downloadFileContent(sysOssFile.getFileUrl());
        String content = new String(contentBytes, StandardCharsets.UTF_8);

        entity.setContent( content);

        return entity;
    }

    @Override
    public SysArticleContentHtmlVo queryHtmlByArticleId(Long id) {
        LambdaQueryWrapper<SysArticleContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysArticleContent::getArticleId, id);
        SysArticleContent sysArticleContent = baseMapper.selectOne(lambdaQueryWrapper);

        SysArticleContentHtmlVo htmlVo = new SysArticleContentHtmlVo();
        BeanUtils.copyProperties(sysArticleContent, htmlVo);

        Long ossId = sysArticleContent.getOssId();
        SysOssFile sysOssFile = ossFileService.queryById(ossId);
        byte[] contentBytes = ossFileService.downloadFileContent(sysOssFile.getFileUrl());
        String markdownContent = new String(contentBytes, StandardCharsets.UTF_8);

        String htmlContent = MarkDownUtil.toKramdownHtml(markdownContent);
        htmlVo.setHtmlContent(htmlContent);

        return htmlVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSysArticleContent(SysArticleContentReq sysArticleContentReq) {
        SysArticleContent sysArticleContent = new SysArticleContent();
        sysArticleContent.setArticleId(sysArticleContentReq.getArticleId());

        Long articleId = sysArticleContentReq.getArticleId();
        SysArticle article = sysArticleService.getById(articleId);

        String content = sysArticleContentReq.getContent();
        // 将 String 转为字节数组
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        String fileName = article.getTitle() + ".md";
        Long ossId = ossFileService.uploadFile(null,fileName, "text/markdown", contentBytes);

        sysArticleContent.setOssId(ossId);

        return baseMapper.insert(sysArticleContent);
    }

    @Override
    public int updateSysArticleContentById(SysArticleContentReq sysArticleContentReq) {

        Long articleId = sysArticleContentReq.getArticleId();
        String content = sysArticleContentReq.getContent();

        LambdaQueryWrapper<SysArticleContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysArticleContent::getArticleId, articleId);

        SysArticleContent sysArticleContent = baseMapper.selectOne(lambdaQueryWrapper);
        Long ossId = sysArticleContent.getOssId();
        SysArticle article = sysArticleService.getById(articleId);

        if (content != null && !content.isEmpty()) {

            //更新历史oss文件
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            String fileName = article.getTitle()+".md";
            ossFileService.uploadFile(ossId,fileName, "text/markdown", contentBytes);

        }


        return baseMapper.updateById(sysArticleContent);
    }

}