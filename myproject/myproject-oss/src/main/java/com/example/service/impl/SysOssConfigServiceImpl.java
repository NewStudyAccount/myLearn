package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.SysOssConfig;
import com.example.mapper.SysOssConfigMapper;
import com.example.service.SysOssConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
* @author AI
* @description 针对表【sys_oss_config】的数据库操作Service实现
* @createDate 2025-07-19 16:06:21
*/
@Service
public class SysOssConfigServiceImpl extends ServiceImpl<SysOssConfigMapper, SysOssConfig>
    implements SysOssConfigService {

    @Autowired
    private SysOssConfigMapper sysOssConfigMapper;



    @Override
    public int insertSysOssConfig(SysOssConfig sysOssConfig) {
        return sysOssConfigMapper.insert(sysOssConfig);
    }

    @Override
    public List<SysOssConfig> listSysOssConfig() {
        return sysOssConfigMapper.selectList( null);
    }

    @Override
    public SysOssConfig querySysOssConfigById(Integer id) {
        LambdaQueryWrapper<SysOssConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysOssConfig::getId,id);
        return sysOssConfigMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public int updateSysOssConfig(SysOssConfig sysOssConfig) {
        LambdaUpdateWrapper<SysOssConfig> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getConfigName()),SysOssConfig::getConfigName,sysOssConfig.getConfigName());
        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getBucketName()),SysOssConfig::getBucketName,sysOssConfig.getBucketName());
        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getAccessKey()),SysOssConfig::getAccessKey,sysOssConfig.getAccessKey());
        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getKeySecret()),SysOssConfig::getKeySecret,sysOssConfig.getKeySecret());
        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getEndPoint()),SysOssConfig::getEndPoint,sysOssConfig.getEndPoint());
        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getRegion()),SysOssConfig::getRegion,sysOssConfig.getRegion());
        lambdaUpdateWrapper.set(StringUtils.isNotEmpty(sysOssConfig.getFileFolder()),SysOssConfig::getFileFolder,sysOssConfig.getFileFolder());
        lambdaUpdateWrapper.set(Objects.nonNull(sysOssConfig.getStatus()),SysOssConfig::getStatus,sysOssConfig.getStatus());
        lambdaUpdateWrapper.eq(SysOssConfig::getConfigName,sysOssConfig.getConfigName());
        lambdaUpdateWrapper.eq(SysOssConfig::getId,sysOssConfig.getId());
        return sysOssConfigMapper.update(lambdaUpdateWrapper);
    }





}




