package com.example.oss.controller;

import com.example.oss.service.CacheRefreshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OSS配置管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/oss/config")
@RequiredArgsConstructor
public class OssConfigController {

    private final CacheRefreshService cacheRefreshService;

    /**
     * 刷新所有OSS配置缓存
     *
     * @return 操作结果
     */
    @PostMapping("/refresh")
    public String refreshCache() {
        log.info("收到刷新OSS配置缓存请求");
        cacheRefreshService.refreshAllOssConfigCache();
        return "OSS配置缓存刷新成功";
    }
}