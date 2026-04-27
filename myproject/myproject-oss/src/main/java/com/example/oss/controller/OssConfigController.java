package com.example.oss.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.req.sysOssConfig.SysOssConfigQueryPageReq;
import com.example.oss.factory.S3OssClientFactory;
import com.example.oss.service.OssConfigService;
import com.example.utils.SnowflakeIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "OSS配置表")
@Slf4j
@RestController
@RequestMapping("/project/sysOssConfig")
@RequiredArgsConstructor
public class OssConfigController {

    @Autowired
    private OssConfigService ossConfigService;

    @Autowired(required = false)
    private S3OssClientFactory s3OssClientFactory;

    @PostMapping("/refresh")
    public String refreshCache() {
        log.info("收到刷新OSS配置缓存请求");
        ossConfigService.initConfig();
        return "OSS配置缓存刷新成功";
    }

    @GetMapping("/cache/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();

        if (s3OssClientFactory != null) {
            var cacheStats = s3OssClientFactory.getCacheStats();
            stats.put("cacheSize", s3OssClientFactory.getCacheSize());
            stats.put("hitRate", String.format("%.2f%%", cacheStats.hitRate() * 100));
            stats.put("hitCount", cacheStats.hitCount());
            stats.put("missCount", cacheStats.missCount());
            stats.put("evictionCount", cacheStats.evictionCount());
            stats.put("loadSuccessCount", cacheStats.loadSuccessCount());
            stats.put("loadFailureCount", cacheStats.loadFailureCount());
            stats.put("averageLoadPenalty", String.format("%.2fms", cacheStats.averageLoadPenalty() / 1_000_000.0));
        } else {
            stats.put("message", "S3客户端工厂未启用");
        }

        return stats;
    }

    @PostMapping("/cache/clear")
    public String clearCache() {
        if (s3OssClientFactory != null) {
            s3OssClientFactory.clearAllClients();
            return "S3客户端缓存已清空";
        }
        return "S3客户端工厂未启用";
    }


    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Response<TableDataInfo<SysOssConfig>> list(@RequestBody SysOssConfigQueryPageReq pageReq) {
        TableDataInfo<SysOssConfig> tableDataInfo = ossConfigService.querySysOssConfigListPage(pageReq);
        return Response.success(tableDataInfo);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Response<SysOssConfig> getById(@PathVariable Long id) {
        SysOssConfig entity = ossConfigService.getById(id);
        return Response.success(entity);
    }

    @Operation(summary = "新增")
    @PostMapping("add")
    public Response<Boolean> save(@RequestBody SysOssConfig entity) {
        long ossNextId = SnowflakeIdUtil.ossNextId();
        entity.setId(ossNextId);
        boolean result = ossConfigService.save(entity);
        return Response.success(result);
    }

    @Operation(summary = "修改")
    @PostMapping("update")
    public Response<Boolean> update(@RequestBody SysOssConfig entity) {
        boolean result = ossConfigService.updateById(entity);
        return Response.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        boolean result = ossConfigService.removeById(id);
        return Response.success(result);
    }
}
