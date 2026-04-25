package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysCategory;
import com.example.domain.req.SysCategoryQueryPageReq;
import com.example.mapper.SysCategoryMapper;
import com.example.service.SysCategoryService;
import org.springframework.stereotype.Service;

@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements SysCategoryService {

    @Override
    public TableDataInfo<SysCategory> querySysCategoryListPage(SysCategoryQueryPageReq pageReq) {
        Page<SysCategory> page = baseMapper.selectPage(pageReq.getPageQuery().build(), null);
        return TableDataInfo.build(page);
    }


    @Override
    public SysCategory queryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int addSysCategory(SysCategory entity) {
        return baseMapper.insert(entity);
    }

    @Override
    public int updateSysCategoryById(SysCategory entity) {
        return baseMapper.updateById(entity);
    }

}
