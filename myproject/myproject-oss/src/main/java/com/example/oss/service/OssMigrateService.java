package com.example.oss.service;

import com.example.domain.TableDataInfo;
import com.example.oss.domain.OssMigrateTask;

import java.util.List;
import java.util.Map;

public interface OssMigrateService {

    Long createTask(String sourceConfig, String targetConfig, List<Long> ossIds, boolean deleteSource);

    void executeAsync(Long taskId);

    Map<String, Object> getProgress(Long taskId);

    void cancelTask(Long taskId);

    void resumeTask(Long taskId);

    TableDataInfo<OssMigrateTask> listTasks(String status, Integer pageNum, Integer pageSize);
}