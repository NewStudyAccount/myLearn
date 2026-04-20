package com.example.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.req.sysOssConfig.SysOssConfigQueryPageReq;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.mapper.OssConfigMapper;
import com.example.oss.service.OssConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OSS配置服务实现类
 */
@Slf4j
@Service
public class OssConfigServiceImpl extends ServiceImpl<OssConfigMapper, SysOssConfig> implements OssConfigService {

    @Autowired
    private OssClientFactory ossClientFactory;



    @Override
    public void initConfig() {
        List<SysOssConfig> sysOssConfigs = listActive();
        for (SysOssConfig sysOssConfig : sysOssConfigs) {
            ossClientFactory.createClient(sysOssConfig);
        }
    }

    @Override
    public SysOssConfig getByConfigName(String configName) {

        SysOssConfig config = baseMapper.selectOne(new LambdaQueryWrapper<SysOssConfig>()
                .eq(SysOssConfig::getConfigName, configName));
        return config;
    }

    @Override
    public List<SysOssConfig> listActive() {

        // 缓存没有，查数据库
        List<SysOssConfig> configs = baseMapper.selectList(new LambdaQueryWrapper<SysOssConfig>()
                .eq(SysOssConfig::getIsActive, true));
        if (configs.size()>1){
            throw new RuntimeException("存在多个启用的配置");
        }

        
        return configs;
    }

    @Override
    public SysOssConfig create(SysOssConfig sysOssConfig) {
        // 验证配置名称唯一性
        SysOssConfig existing = getByConfigName(sysOssConfig.getConfigName());
        if (existing != null) {
            throw new RuntimeException("配置名称已存在: " + sysOssConfig.getConfigName());
        }
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        sysOssConfig.setCreatedAt(now);
        sysOssConfig.setUpdatedAt(now);
        
        // 默认启用
        if (sysOssConfig.getIsActive() == null) {
            sysOssConfig.setIsActive(true);
        }
        
        baseMapper.insert(sysOssConfig);
        

        
        log.info("创建OSS配置成功: configName={}", sysOssConfig.getConfigName());
        return sysOssConfig;
    }

    @Override
    public SysOssConfig update(SysOssConfig sysOssConfig) {
        // 检查配置是否存在
        SysOssConfig existing = getById(sysOssConfig.getId());
        if (existing == null) {
            throw new RuntimeException("配置不存在: " + sysOssConfig.getId());
        }
        
        // 如果修改了配置名称，检查新名称是否已存在
        if (!existing.getConfigName().equals(sysOssConfig.getConfigName())) {
            SysOssConfig nameExists = getByConfigName(sysOssConfig.getConfigName());
            if (nameExists != null) {
                throw new RuntimeException("配置名称已存在: " + sysOssConfig.getConfigName());
            }
        }
        
        // 更新时间
        sysOssConfig.setUpdatedAt(LocalDateTime.now());
        
        baseMapper.updateById(sysOssConfig);
        

        
        log.info("更新OSS配置成功: id={}", sysOssConfig.getId());
        return getById(sysOssConfig.getId());
    }

    @Override
    public boolean delete(Long id) {
        // 检查配置是否存在
        SysOssConfig existing = getById(id);
        if (existing == null) {
            throw new RuntimeException("配置不存在: " + id);
        }
        
        boolean result = baseMapper.deleteById(id) > 0;

        
        return result;
    }

    @Override
    public TableDataInfo<SysOssConfig> querySysOssConfigListPage(SysOssConfigQueryPageReq pageReq) {
        LambdaQueryWrapper<SysOssConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(pageReq.getConfigName()),SysOssConfig::getConfigName, pageReq.getConfigName())
                .eq(StringUtils.isNotEmpty(pageReq.getProvider()),SysOssConfig::getProvider, pageReq.getProvider())
                .eq(StringUtils.isNotEmpty(pageReq.getBucketName()),SysOssConfig::getBucketName, pageReq.getBucketName())
                .eq(pageReq.getIsActive()!=null,SysOssConfig::getIsActive, pageReq.getIsActive());
        Page<SysOssConfig> sysOssConfigPage = this.baseMapper.selectPage(pageReq.getPageQuery().build(), queryWrapper);
        return TableDataInfo.build(sysOssConfigPage);
    }


}