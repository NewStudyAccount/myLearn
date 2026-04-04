package com.example.oss.facade;

import com.example.factory.OssFactory;
import com.example.oss.service.OssClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * OSS客户端门面实现类
 * 优先使用新的动态配置服务，如果失败则回退到旧的静态配置方式
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssClientFacadeImpl implements OssClientFacade {

    private final OssClientService ossClientService;
    private final OssFactory ossFactory;

    @Override
    public Object getClient(String configName) {
        try {
            // 优先使用新的动态配置服务
            return ossClientService.getClient(configName);
        } catch (Exception e) {
            log.warn("使用动态配置获取OSS客户端失败，尝试旧方式: {}", e.getMessage());
            // 回退到旧方式（这里需要根据configName查找对应的ID，但旧方式使用ID）
            // 由于旧方式使用ID，而新方式使用configName，需要额外的映射逻辑
            // 暂时抛出异常，后续可以完善
            throw new UnsupportedOperationException("暂不支持通过configName回退到旧方式，请使用getDefaultClient()");
        }
    }

    @Override
    public Object getDefaultClient() {
        try {
            // 尝试使用新的动态配置服务获取默认配置
            // 这里需要知道默认配置的名称，可以约定为"default"或从配置中读取
            return ossClientService.getClient("default");
        } catch (Exception e) {
            log.warn("使用动态配置获取默认OSS客户端失败，回退到旧方式: {}", e.getMessage());
            // 回退到旧方式
            return ossFactory.getDefaultOssClient();
        }
    }

    @Override
    public boolean testConnection(String configName) {
        try {
            return ossClientService.testConnection(configName);
        } catch (Exception e) {
            log.warn("测试OSS连接失败: {}", e.getMessage());
            return false;
        }
    }
}