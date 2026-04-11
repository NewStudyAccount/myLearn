package com.example.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oss.cache.OssConfigCacheService;
import com.example.oss.domain.OssConfig;
import com.example.oss.mapper.OssConfigMapper;
import com.example.oss.service.OssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OSS配置服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssConfigServiceImpl extends ServiceImpl<OssConfigMapper, OssConfig> implements OssConfigService {


    @Override
    public OssConfig getByConfigName(String configName) {

        OssConfig config = baseMapper.selectOne(new LambdaQueryWrapper<OssConfig>()
                .eq(OssConfig::getConfigName, configName));
        

        
        return config;
    }

    @Override
    public List<OssConfig> listActive() {

        // 缓存没有，查数据库
        List<OssConfig> configs = baseMapper.selectList(new LambdaQueryWrapper<OssConfig>()
                .eq(OssConfig::getIsActive, true));

        
        return configs;
    }

    @Override
    public OssConfig create(OssConfig ossConfig) {
        // 验证配置名称唯一性
        OssConfig existing = getByConfigName(ossConfig.getConfigName());
        if (existing != null) {
            throw new RuntimeException("配置名称已存在: " + ossConfig.getConfigName());
        }
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        ossConfig.setCreatedAt(now);
        ossConfig.setUpdatedAt(now);
        
        // 默认启用
        if (ossConfig.getIsActive() == null) {
            ossConfig.setIsActive(true);
        }
        
        baseMapper.insert(ossConfig);
        

        
        log.info("创建OSS配置成功: configName={}", ossConfig.getConfigName());
        return ossConfig;
    }

    @Override
    public OssConfig update(OssConfig ossConfig) {
        // 检查配置是否存在
        OssConfig existing = getById(ossConfig.getId());
        if (existing == null) {
            throw new RuntimeException("配置不存在: " + ossConfig.getId());
        }
        
        // 如果修改了配置名称，检查新名称是否已存在
        if (!existing.getConfigName().equals(ossConfig.getConfigName())) {
            OssConfig nameExists = getByConfigName(ossConfig.getConfigName());
            if (nameExists != null) {
                throw new RuntimeException("配置名称已存在: " + ossConfig.getConfigName());
            }
        }
        
        // 更新时间
        ossConfig.setUpdatedAt(LocalDateTime.now());
        
        baseMapper.updateById(ossConfig);
        

        
        log.info("更新OSS配置成功: id={}", ossConfig.getId());
        return getById(ossConfig.getId());
    }

    @Override
    public boolean delete(Long id) {
        // 检查配置是否存在
        OssConfig existing = getById(id);
        if (existing == null) {
            throw new RuntimeException("配置不存在: " + id);
        }
        
        boolean result = baseMapper.deleteById(id) > 0;

        
        return result;
    }
}