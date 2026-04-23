package com.example.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oss.domain.SysOssFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * OSS配置Mapper接口
 */
@Mapper
public interface OssFileMapper extends BaseMapper<SysOssFile> {
}