package com.example.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysArticle;
import com.example.domain.req.SysArticleQueryPageReq;
import com.example.domain.req.SysArticleReq;
import com.example.mapper.SysArticleMapper;
import com.example.service.SysArticleService;
import com.example.utils.SnowflakeIdUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysArticleServiceImpl extends ServiceImpl<SysArticleMapper, SysArticle> implements SysArticleService {

    @Override
    public TableDataInfo<SysArticle> querySysArticleListPage(SysArticleQueryPageReq pageReq) {
        Page<SysArticle> page = baseMapper.selectPage(pageReq.getPageQuery().build(), null);
        return TableDataInfo.build(page);
    }


    @Override
    public SysArticle queryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int addSysArticle(SysArticle entity) {
        long blogNextId = SnowflakeIdUtil.blogNextId();
        entity.setId(blogNextId);
        return baseMapper.insert(entity);
    }

    @Override
    public int updateSysArticleById(SysArticle entity) {
        return baseMapper.updateById(entity);
    }

}
