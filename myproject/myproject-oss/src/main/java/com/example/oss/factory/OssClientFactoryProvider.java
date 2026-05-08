package com.example.oss.factory;

import com.example.oss.domain.SysOssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OssClientFactoryProvider {

    private final Map<String, OssClientFactory> factoryMap;

    public OssClientFactoryProvider(List<OssClientFactory> factories) {
        this.factoryMap = factories.stream()
                .collect(Collectors.toMap(OssClientFactory::getProvider, f -> f));
        log.info("OssClientFactoryProvider 初始化完成，已注册工厂: {}", factoryMap.keySet());
    }

    public OssClientFactory getFactory(String provider) {
        OssClientFactory factory = factoryMap.get(provider);
        if (factory == null) {
            throw new RuntimeException("不支持的OSS提供商: " + provider);
        }
        return factory;
    }

    public OssClientFactory getFactory(SysOssConfig config) {
        return getFactory(config.getProvider());
    }
}