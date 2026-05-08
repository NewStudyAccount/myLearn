package com.example.oss.controller;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.OssMigrateTask;
import com.example.oss.service.OssMigrateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "OSS迁移管理")
@RestController
@RequestMapping("/project/oss/migrate")
@RequiredArgsConstructor
public class OssMigrateController {

    private final OssMigrateService ossMigrateService;

    @Operation(summary = "创建迁移任务")
    @PostMapping("/task")
    public Response<Long> createTask(
            @RequestParam String sourceConfig,
            @RequestParam String targetConfig,
            @RequestParam(required = false) List<Long> ossIds,
            @RequestParam(defaultValue = "false") boolean deleteSource) {
        Long taskId = ossMigrateService.createTask(sourceConfig, targetConfig, ossIds, deleteSource);
        ossMigrateService.executeAsync(taskId);
        return Response.success(taskId);
    }

    @Operation(summary = "查询迁移进度")
    @GetMapping("/task/{taskId}/progress")
    public Response<Map<String, Object>> getProgress(@PathVariable Long taskId) {
        return Response.success(ossMigrateService.getProgress(taskId));
    }

    @Operation(summary = "取消迁移任务")
    @PostMapping("/task/{taskId}/cancel")
    public Response<Void> cancelTask(@PathVariable Long taskId) {
        ossMigrateService.cancelTask(taskId);
        return Response.success();
    }

    @Operation(summary = "续传迁移任务")
    @PostMapping("/task/{taskId}/resume")
    public Response<Void> resumeTask(@PathVariable Long taskId) {
        ossMigrateService.resumeTask(taskId);
        return Response.success();
    }

    @Operation(summary = "查询迁移任务列表")
    @GetMapping("/task/list")
    public Response<TableDataInfo<OssMigrateTask>> listTasks(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Response.success(ossMigrateService.listTasks(status, pageNum, pageSize));
    }
}