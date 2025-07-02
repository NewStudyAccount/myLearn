package com.example.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.pojo.SysArticleTagRel;
import com.example.domain.req.SysArticleTagRelReq;
import com.example.domain.vo.SysArticleTagRelVo;
import com.example.mapper.SysArticleTagRelMapper;
import com.example.service.SysArticleTagRelService;
import com.example.utils.SnowflakeIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_article_tag_rel(文章标签关系表)】的数据库操作Service实现
* @createDate 2025-04-01 23:31:09
*/
@Service
public class SysArticleTagRelServiceImpl extends ServiceImpl<SysArticleTagRelMapper, SysArticleTagRel>
    implements SysArticleTagRelService {

    @Autowired
    private SysArticleTagRelMapper sysArticleTagRelMapper;

    @Override
    public List<SysArticleTagRel> queryArticleTagRelList(Long articleId) {
        LambdaQueryWrapper<SysArticleTagRel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysArticleTagRel::getArticleId,articleId);
        List<SysArticleTagRel> sysArticleTagRels = sysArticleTagRelMapper.selectList(lambdaQueryWrapper);

//        if (!CollectionUtils.isEmpty(sysArticleTagRels)) {
//            for (SysArticleTagRel sysArticleTagRel : sysArticleTagRels) {
//                SysArticleTagRelVo sysArticleTagRelVo = new SysArticleTagRelVo();
//                BeanUtils.copyProperties(sysArticleTagRel,sysArticleTagRelVo);
//
//                //todo 获取标签名称  不应该在这里在获取了，标签、分类应该在系统启动时初始化。前端启动时就已经获取了
//                sysArticleTagRelVo.setTagName(null);
//            }
//        }

        return sysArticleTagRels;
    }

    @Override
    public int addArticleTagRel(SysArticleTagRelReq sysArticleTagRelReq) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1);
        SysArticleTagRel sysArticleTagRel = new SysArticleTagRel();

        BeanUtils.copyProperties(sysArticleTagRelReq,sysArticleTagRel);
        sysArticleTagRel.setId(snowflakeIdGenerator.nextId());
        return sysArticleTagRelMapper.insert(sysArticleTagRel);
    }

    @Override
    public int deleteArticleTagRel(Long id) {
        LambdaQueryWrapper<SysArticleTagRel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysArticleTagRel::getId,id);
        return sysArticleTagRelMapper.delete(lambdaQueryWrapper);
    }
}




