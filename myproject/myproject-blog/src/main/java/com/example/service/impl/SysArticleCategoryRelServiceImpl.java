package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.pojo.SysArticleCategoryRel;
import com.example.domain.req.SysArticleCategoryRelReq;
import com.example.mapper.SysArticleCategoryRelMapper;
import com.example.service.SysArticleCategoryRelService;
import com.example.utils.SnowflakeIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_article_category_rel】的数据库操作Service实现
* @createDate 2025-04-01 23:31:09
*/
@Service
public class SysArticleCategoryRelServiceImpl extends ServiceImpl<SysArticleCategoryRelMapper, SysArticleCategoryRel>
    implements SysArticleCategoryRelService {


    @Autowired
    private SysArticleCategoryRelMapper sysArticleCategoryRelMapper;

    @Override
    public List<SysArticleCategoryRel> queryArticleCategoryRelList(Long articleId) {
        LambdaQueryWrapper<SysArticleCategoryRel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysArticleCategoryRel::getArticleId,articleId);
        List<SysArticleCategoryRel> sysArticleCategoryRels = sysArticleCategoryRelMapper.selectList(lambdaQueryWrapper);

        return sysArticleCategoryRels;
    }

    @Override
    public int addArticleCategoryRel(SysArticleCategoryRelReq sysArticleCategoryRelReq) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1);
        SysArticleCategoryRel sysArticleCategoryRel = new SysArticleCategoryRel();

        BeanUtils.copyProperties(sysArticleCategoryRelReq,sysArticleCategoryRel);
        sysArticleCategoryRel.setId(snowflakeIdGenerator.nextId());
        return sysArticleCategoryRelMapper.insert(sysArticleCategoryRel);
    }

    @Override
    public int deleteArticleCategoryRel(SysArticleCategoryRel sysArticleCategoryRel) {
        return 0;
    }
}




