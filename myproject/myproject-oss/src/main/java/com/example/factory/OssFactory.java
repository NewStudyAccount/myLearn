package com.example.factory;

import com.example.ConsTant.OssConstant;
import com.example.config.OssClient;
import com.example.domain.SysOssConfig;
import com.example.redis.RedisCache;
import com.example.service.SysOssConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OssFactory {


    private static final Map<String, OssClient> CLIENT_CACHE = new ConcurrentHashMap<>();


    @Autowired
    private SysOssConfigService sysOssConfigService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 初始化 OSSClient
     */
    public void init() {
        List<SysOssConfig> sysOssConfigs = sysOssConfigService.listSysOssConfig();
        if (CollectionUtils.isEmpty(sysOssConfigs)){
            return;
        }
        for (SysOssConfig sysOssConfig : sysOssConfigs) {
            Integer status = sysOssConfig.getStatus();
            // 0:默认，按照默认配置创建OssClient
            if (status == 0){
                redisCache.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY,sysOssConfig.getId());
                CLIENT_CACHE.put(sysOssConfig.getId().toString(), new OssClient(sysOssConfig));
            }
            //将所有配置都加入到缓存中
            redisCache.setCacheObject(OssConstant.CONFIG_KEY_PREFIX + sysOssConfig.getId(),sysOssConfig);

        }

    }

    public OssClient getOssClientDynamic(Integer id) {
        SysOssConfig sysOssConfig = redisCache.getCacheObject(OssConstant.CONFIG_KEY_PREFIX + id);
        OssClient ossClient = CLIENT_CACHE.get(id.toString());
        if (Objects.isNull(ossClient)){
            // 缓存中没有，则创建
            ossClient = new OssClient(sysOssConfig);
            CLIENT_CACHE.put(sysOssConfig.getId().toString(), ossClient);
        }
        if (!ossClient.checkPropertiesSame(sysOssConfig)){
            // 配置项有更新，则重新创建，并更新缓存数据
            ossClient = new OssClient(sysOssConfig);
            CLIENT_CACHE.put(sysOssConfig.getId().toString(), ossClient);
            redisCache.setCacheObject(OssConstant.CONFIG_KEY_PREFIX + sysOssConfig.getId(),sysOssConfig);
        }
        return ossClient;
    }

    public OssClient getDefaultOssClient() {
        String string = redisCache.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY).toString();
        OssClient ossClient = getOssClientDynamic(Integer.parseInt(string));
        return ossClient;
    }


}
