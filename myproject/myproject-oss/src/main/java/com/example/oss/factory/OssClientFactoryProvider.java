package com.example.oss.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * OSS客户端工厂提供者
 */
@Slf4j
@Component
public class OssClientFactoryProvider {

    private final ConcurrentHashMap<String, OssClientFactory> factoryMap;

    public OssClientFactoryProvider(List<OssClientFactory> factories) {
        // 将工厂列表转换为Map，key为提供商类型
        this.factoryMap = (ConcurrentHashMap<String, OssClientFactory>) factories.stream()
                .collect(Collectors.toMap(OssClientFactory::getProvider, Function.identity()));
        
        log.info("已注册OSS客户端工厂: {}", factoryMap.keySet());
    }

    /**
     * 根据提供商类型获取对应的工厂
     *
     * @param provider 提供商类型
     * @return 客户端工厂
     */
    public OssClientFactory getFactory(String provider) {
        OssClientFactory factory = factoryMap.get(provider);
        if (factory == null) {
            throw new IllegalArgumentException("不支持的OSS提供商类型: " + provider);
        }
        return factory;
    }

    /**
     * 检查是否支持指定的提供商类型
     *
     * @param provider 提供商类型
     * @return 是否支持
     */
    public boolean supports(String provider) {
        return factoryMap.containsKey(provider);
    }

    /**
     * 获取所有支持的提供商类型
     *
     * @return 提供商类型集合
     */
    public java.util.Set<String> getSupportedProviders() {
        return factoryMap.keySet();
    }
}