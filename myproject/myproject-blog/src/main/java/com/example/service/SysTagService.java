package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.TableDataInfo;
import com.example.domain.pojo.SysTag;
import com.example.domain.req.SysTagQueryPageReq;

public interface SysTagService extends IService<SysTag> {

    TableDataInfo<SysTag> querySysTagListPage(SysTagQueryPageReq pageReq);

    SysTag queryById(Long id);

    int addSysTag(SysTag entity);

    int updateSysTagById(SysTag entity);
}
