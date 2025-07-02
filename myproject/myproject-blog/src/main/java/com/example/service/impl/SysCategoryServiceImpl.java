package com.example.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysCategory;
import com.example.domain.req.SysCategoryQueryPageReq;
import com.example.domain.req.SysCategoryReq;
import com.example.mapper.SysCategoryMapper;
import com.example.service.SysCategoryService;
import com.example.utils.SnowflakeIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_category(文章分类)】的数据库操作Service实现
* @createDate 2025-04-01 23:31:09
*/
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory>
    implements SysCategoryService {

    @Autowired
    private SysCategoryMapper sysCategoryMapper;

    @Override
    public TableDataInfo<SysCategory> queryCategoryListPage(SysCategoryQueryPageReq sysCategoryQueryPageReq) {
        Page<SysCategory> sysCategoryPage = sysCategoryMapper.selectPage(sysCategoryQueryPageReq.getPageQuery().build(), null);
        TableDataInfo<SysCategory> build = TableDataInfo.build(sysCategoryPage);
        return build;
    }

    @Override
    public List<SysCategory> queryCategoryList() {
        return sysCategoryMapper.selectList(null);
    }

    @Override
    public int addCategory(SysCategoryReq sysCategoryReq) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1);
        long seqId = snowflakeIdGenerator.nextId();
        SysCategory sysCategory = new SysCategory();
        BeanUtils.copyProperties(sysCategoryReq,sysCategory);
        sysCategory.setId(seqId);
        return sysCategoryMapper.insert(sysCategory);
    }

    @Override
    public int deleteCategory(Long categoryId) {
        return sysCategoryMapper.deleteById(categoryId);
    }
}




