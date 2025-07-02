package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.pojo.SysCategory;
import com.example.domain.TableDataInfo;
import com.example.domain.req.SysCategoryQueryPageReq;
import com.example.domain.req.SysCategoryReq;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_category(文章分类)】的数据库操作Service
* @createDate 2025-04-01 23:31:09
*/
public interface SysCategoryService extends IService<SysCategory> {
    TableDataInfo<SysCategory> queryCategoryListPage(SysCategoryQueryPageReq sysCategoryQueryPageReq);

    List<SysCategory> queryCategoryList();

    int addCategory(SysCategoryReq sysCategoryReq);

    int deleteCategory(Long categoryId);

}
