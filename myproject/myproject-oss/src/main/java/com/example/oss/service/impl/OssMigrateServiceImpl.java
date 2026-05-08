package com.example.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.PageQuery;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.OssMigrateTask;
import com.example.oss.domain.OssMigrateTaskItem;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.SysOssFile;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.factory.OssClientFactoryProvider;
import com.example.oss.mapper.OssFileMapper;
import com.example.oss.mapper.OssMigrateTaskItemMapper;
import com.example.oss.mapper.OssMigrateTaskMapper;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssMigrateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OssMigrateServiceImpl implements OssMigrateService {

    @Autowired
    private OssMigrateTaskMapper taskMapper;

    @Autowired
    private OssMigrateTaskItemMapper taskItemMapper;

    @Autowired
    private OssFileMapper ossFileMapper;

    @Autowired
    private OssConfigService ossConfigService;

    @Autowired
    private OssClientFactoryProvider factoryProvider;

    @Lazy
    @Autowired
    private OssMigrateService self;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(String sourceConfig, String targetConfig, List<Long> ossIds, boolean deleteSource) {
        SysOssConfig sourceOssConfig = ossConfigService.getByConfigName(sourceConfig);
        if (sourceOssConfig == null) {
            throw new RuntimeException("源OSS配置不存在: " + sourceConfig);
        }
        SysOssConfig targetOssConfig = ossConfigService.getByConfigName(targetConfig);
        if (targetOssConfig == null) {
            throw new RuntimeException("目标OSS配置不存在: " + targetConfig);
        }
        if (!Boolean.TRUE.equals(targetOssConfig.getIsActive())) {
            throw new RuntimeException("目标OSS配置未启用: " + targetConfig);
        }
        if (sourceConfig.equals(targetConfig)) {
            throw new RuntimeException("源配置和目标配置不能相同");
        }

        OssMigrateTask task = new OssMigrateTask();
        task.setSourceConfig(sourceConfig);
        task.setTargetConfig(targetConfig);
        task.setStatus(OssMigrateTask.STATUS_PENDING);
        task.setTotalCount(0);
        task.setSuccessCount(0);
        task.setFailCount(0);
        task.setDeleteSource(deleteSource);
        task.setCreatedAt(LocalDateTime.now());
        taskMapper.insert(task);

        List<SysOssFile> files;
        if (ossIds != null && !ossIds.isEmpty()) {
            files = ossFileMapper.selectBatchIds(ossIds);
        } else {
            String urlPrefix = sourceOssConfig.getEndpoint() + "/" + sourceOssConfig.getBucketName();
            LambdaQueryWrapper<SysOssFile> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(SysOssFile::getFileUrl, urlPrefix);
            files = ossFileMapper.selectList(wrapper);
        }

        for (SysOssFile file : files) {
            OssMigrateTaskItem item = new OssMigrateTaskItem();
            item.setTaskId(task.getTaskId());
            item.setOssId(file.getOssId());
            item.setStatus(OssMigrateTaskItem.STATUS_PENDING);
            item.setSourceUrl(file.getFileUrl());
            item.setCreatedAt(LocalDateTime.now());
            taskItemMapper.insert(item);
        }

        task.setTotalCount(files.size());
        taskMapper.updateById(task);

        log.info("创建迁移任务: taskId={}, source={}, target={}, totalCount={}, deleteSource={}",
                task.getTaskId(), sourceConfig, targetConfig, files.size(), deleteSource);

        return task.getTaskId();
    }

    @Override
    @Async("ossMigrateExecutor")
    public void executeAsync(Long taskId) {
        OssMigrateTask task = taskMapper.selectById(taskId);
        if (task == null) {
            log.error("迁移任务不存在: {}", taskId);
            return;
        }

        task.setStatus(OssMigrateTask.STATUS_RUNNING);
        taskMapper.updateById(task);

        LambdaQueryWrapper<OssMigrateTaskItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OssMigrateTaskItem::getTaskId, taskId)
                .eq(OssMigrateTaskItem::getStatus, OssMigrateTaskItem.STATUS_PENDING);
        List<OssMigrateTaskItem> pendingItems = taskItemMapper.selectList(wrapper);

        for (OssMigrateTaskItem item : pendingItems) {
            task = taskMapper.selectById(taskId);
            if (task == null || OssMigrateTask.STATUS_CANCELLED.equals(task.getStatus())) {
                log.info("迁移任务已取消，停止执行: taskId={}", taskId);
                return;
            }

            migrateSingleFile(task, item);
        }

        task = taskMapper.selectById(taskId);
        if (task != null && !OssMigrateTask.STATUS_CANCELLED.equals(task.getStatus())) {
            task.setStatus(OssMigrateTask.STATUS_COMPLETED);
            task.setFinishedAt(LocalDateTime.now());
            taskMapper.updateById(task);
            log.info("迁移任务完成: taskId={}, success={}, fail={}",
                    taskId, task.getSuccessCount(), task.getFailCount());
        }
    }

    private void migrateSingleFile(OssMigrateTask task, OssMigrateTaskItem item) {
        try {
            SysOssConfig sourceConfig = ossConfigService.getByConfigName(task.getSourceConfig());
            SysOssConfig targetConfig = ossConfigService.getByConfigName(task.getTargetConfig());
            OssClientFactory sourceFactory = factoryProvider.getFactory(sourceConfig);
            OssClientFactory targetFactory = factoryProvider.getFactory(targetConfig);

            String sourcePrefix = sourceConfig.getEndpoint() + "/" + sourceConfig.getBucketName() + "/";
            String objectName;
            if (item.getSourceUrl().startsWith(sourcePrefix)) {
                objectName = item.getSourceUrl().substring(sourcePrefix.length());
            } else {
                throw new RuntimeException("文件URL与源配置不匹配: " + item.getSourceUrl());
            }

            long fileSize = sourceFactory.getFileSize(sourceConfig, objectName);
            InputStream inputStream = sourceFactory.downloadFileStream(sourceConfig, objectName);

            String newObjectName = objectName;
            String targetUrl;
            try {
                targetFactory.uploadFile(targetConfig, newObjectName,
                        getContentType(item.getOssId()), inputStream, fileSize);
                String targetPrefix = targetConfig.getEndpoint() + "/" + targetConfig.getBucketName() + "/";
                targetUrl = targetPrefix + newObjectName;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("关闭输入流失败: {}", e.getMessage());
                }
            }

            SysOssFile ossFile = ossFileMapper.selectById(item.getOssId());
            if (ossFile != null) {
                ossFile.setFileUrl(targetUrl);
                ossFileMapper.updateById(ossFile);
            }

            if (Boolean.TRUE.equals(task.getDeleteSource())) {
                try {
                    sourceFactory.deleteFile(sourceConfig, objectName);
                } catch (Exception e) {
                    log.warn("删除源端文件失败(不影响迁移结果): objectName={}, error={}", objectName, e.getMessage());
                }
            }

            item.setStatus(OssMigrateTaskItem.STATUS_SUCCESS);
            item.setTargetUrl(targetUrl);
            taskItemMapper.updateById(item);

            task.setSuccessCount(task.getSuccessCount() + 1);
            taskMapper.updateById(task);

        } catch (Exception e) {
            log.error("文件迁移失败: ossId={}, error={}", item.getOssId(), e.getMessage(), e);
            item.setStatus(OssMigrateTaskItem.STATUS_FAILED);
            item.setErrorMsg(e.getMessage());
            taskItemMapper.updateById(item);

            task.setFailCount(task.getFailCount() + 1);
            taskMapper.updateById(task);
        }
    }

    private String getContentType(Long ossId) {
        SysOssFile file = ossFileMapper.selectById(ossId);
        return file != null && file.getContentType() != null ? file.getContentType() : "application/octet-stream";
    }

    @Override
    public Map<String, Object> getProgress(Long taskId) {
        OssMigrateTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("迁移任务不存在: " + taskId);
        }

        Map<String, Object> progress = new HashMap<>();
        progress.put("taskId", task.getTaskId());
        progress.put("status", task.getStatus());
        progress.put("totalCount", task.getTotalCount());
        progress.put("successCount", task.getSuccessCount());
        progress.put("failCount", task.getFailCount());

        int processed = task.getSuccessCount() + task.getFailCount();
        int percent = task.getTotalCount() > 0 ? (processed * 100 / task.getTotalCount()) : 0;
        progress.put("percent", percent);
        progress.put("remaining", task.getTotalCount() - processed);
        progress.put("sourceConfig", task.getSourceConfig());
        progress.put("targetConfig", task.getTargetConfig());
        progress.put("createdAt", task.getCreatedAt());
        progress.put("finishedAt", task.getFinishedAt());

        return progress;
    }

    @Override
    public void cancelTask(Long taskId) {
        OssMigrateTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("迁移任务不存在: " + taskId);
        }
        if (OssMigrateTask.STATUS_COMPLETED.equals(task.getStatus())) {
            throw new RuntimeException("任务已完成，无法取消");
        }
        if (OssMigrateTask.STATUS_CANCELLED.equals(task.getStatus())) {
            throw new RuntimeException("任务已取消");
        }

        task.setStatus(OssMigrateTask.STATUS_CANCELLED);
        task.setFinishedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        log.info("迁移任务已取消: taskId={}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeTask(Long taskId) {
        OssMigrateTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("迁移任务不存在: " + taskId);
        }
        if (OssMigrateTask.STATUS_COMPLETED.equals(task.getStatus())) {
            throw new RuntimeException("任务已完成，无需续传");
        }
        if (OssMigrateTask.STATUS_RUNNING.equals(task.getStatus())) {
            throw new RuntimeException("任务正在执行中");
        }

        task.setStatus(OssMigrateTask.STATUS_PENDING);
        taskMapper.updateById(task);

        LambdaUpdateWrapper<OssMigrateTaskItem> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OssMigrateTaskItem::getTaskId, taskId)
                .eq(OssMigrateTaskItem::getStatus, OssMigrateTaskItem.STATUS_FAILED)
                .set(OssMigrateTaskItem::getStatus, OssMigrateTaskItem.STATUS_PENDING)
                .set(OssMigrateTaskItem::getErrorMsg, null);
        taskItemMapper.update(null, updateWrapper);

        log.info("迁移任务续传: taskId={}", taskId);
        self.executeAsync(taskId);
    }

    @Override
    public TableDataInfo<OssMigrateTask> listTasks(String status, Integer pageNum, Integer pageSize) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum != null ? pageNum : 1);
        pageQuery.setPageSize(pageSize != null ? pageSize : 10);

        LambdaQueryWrapper<OssMigrateTask> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OssMigrateTask::getStatus, status);
        }
        wrapper.orderByDesc(OssMigrateTask::getCreatedAt);

        Page<OssMigrateTask> page = taskMapper.selectPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }
}