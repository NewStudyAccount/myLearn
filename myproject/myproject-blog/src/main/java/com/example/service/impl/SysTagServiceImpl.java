package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysTag;
import com.example.domain.req.SysTagQueryPageReq;
import com.example.mapper.SysTagMapper;
import com.example.service.SysTagService;
import org.springframework.stereotype.Service;

@Service
public class SysTagServiceImpl extends ServiceImpl<SysTagMapper, SysTag> implements SysTagService {

    @Override
    public TableDataInfo<SysTag> querySysTagListPage(SysTagQueryPageReq pageReq) {
        Page<SysTag> page = baseMapper.selectPage(pageReq.getPageQuery().build(), null);
        return TableDataInfo.build(page);
    }


    @Override
    public SysTag queryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int addSysTag(SysTag entity) {
        return baseMapper.insert(entity);
    }

    @Override
    public int updateSysTagById(SysTag entity) {
        return baseMapper.updateById(entity);
    }

}
