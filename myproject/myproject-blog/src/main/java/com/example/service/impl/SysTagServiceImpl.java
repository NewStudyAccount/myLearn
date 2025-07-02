package com.example.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.pojo.SysTag;
import com.example.domain.TableDataInfo;
import com.example.domain.req.SysTagQueryPageReq;
import com.example.domain.req.SysTagReq;
import com.example.execption.BizException;
import com.example.execption.ResponseCodeEnum;
import com.example.mapper.SysTagMapper;
import com.example.service.SysTagService;
import com.example.utils.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author QJJ
* @description 针对表【sys_tag(标签)】的数据库操作Service实现
* @createDate 2025-04-01 23:31:09
*/
@Slf4j
@Service
public class SysTagServiceImpl extends ServiceImpl<SysTagMapper, SysTag>
    implements SysTagService {

    @Autowired
    private SysTagMapper sysTagMapper;

    @Override
    public TableDataInfo<SysTag> queryTagListPage(SysTagQueryPageReq sysTagQueryPageReq) {
        Page<SysTag> sysTagPage = sysTagMapper.selectPage(sysTagQueryPageReq.getPageQuery().build(), null);
        return TableDataInfo.build(sysTagPage);
    }

    @Override
    public List<SysTag> queryTagList() {
        return sysTagMapper.selectList( null);
    }

    @Override
    public int addTag(SysTagReq sysTagReq) {
        try {
            SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);
            long seqId = idGenerator.nextId();
            SysTag sysTag = new SysTag();

            BeanUtils.copyProperties(sysTagReq, sysTag);
            sysTag.setId(seqId);
            return sysTagMapper.insert(sysTag);
        } catch (Exception e) {
            log.error("添加标签异常{}", e.getMessage());
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public int deleteTag(Long tagId) {
        return sysTagMapper.deleteById(tagId);
    }
}




