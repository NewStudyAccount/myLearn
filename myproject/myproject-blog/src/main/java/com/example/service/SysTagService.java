package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.pojo.SysTag;
import com.example.domain.TableDataInfo;
import com.example.domain.req.SysTagQueryPageReq;
import com.example.domain.req.SysTagReq;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_tag(标签)】的数据库操作Service
* @createDate 2025-04-01 23:31:09
*/
public interface SysTagService extends IService<SysTag> {
    TableDataInfo<SysTag> queryTagListPage(SysTagQueryPageReq sysTagQueryPageReq);

    List<SysTag> queryTagList();

    int addTag(SysTagReq sysTagReq);

    int deleteTag(Long tagId);

}
