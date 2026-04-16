package com.example.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oss.domain.SysOssConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * OSS配置Mapper接口
 */
@Mapper
public interface OssConfigMapper extends BaseMapper<SysOssConfig> {
}