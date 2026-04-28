package com.example.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticleContent;
import com.example.domain.req.SysArticleContentQueryPageReq;
import com.example.mapper.SysArticleContentMapper;
import com.example.oss.service.OssFileService;
import com.example.service.SysArticleContentService;
import com.example.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class SysArticleContentServiceImpl extends ServiceImpl<SysArticleContentMapper, SysArticleContent> implements SysArticleContentService {


    @Autowired
    private OssFileService ossFileService;

    @Override
    public TableDataInfo<SysArticleContent> querySysArticleContentListPage(SysArticleContentQueryPageReq pageReq) {
        Page<SysArticleContent> page = baseMapper.selectPage(pageReq.getPageQuery().build(), null);
        return TableDataInfo.build(page);
    }


    @Override
    public SysArticleContent queryById(Long id) {
        SysArticleContent sysArticleContent = baseMapper.selectById(id);
        String contentUrl = sysArticleContent.getContentUrl();

        byte[] contentBytes = ossFileService.downloadFileContent(contentUrl);
        String content = new String(contentBytes, StandardCharsets.UTF_8);
        sysArticleContent.setContent( content);

        return sysArticleContent;

    }

    @Override
    public int addSysArticleContent(SysArticleContent entity) {
        long blogNextId = SnowflakeIdUtil.blogNextId();
        entity.setId(blogNextId);
        String content = entity.getContent();
        // 将 String 转为字节数组
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        String fileName = "ssss.md";
        String contentUrl = ossFileService.uploadFile(fileName, "text/markdown", contentBytes);

        entity.setContentUrl(contentUrl);

        return baseMapper.insert(entity);
    }

    @Override
    public int updateSysArticleContentById(SysArticleContent entity) {

        String content = entity.getContent();
        if (content != null && !content.isEmpty()) {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            String fileName = "article/" + entity.getId() + "/content.md";
            String contentUrl = ossFileService.uploadFile(fileName, "text/markdown", contentBytes);
            entity.setContentUrl(contentUrl);
        }


        return baseMapper.updateById(entity);
    }

}
