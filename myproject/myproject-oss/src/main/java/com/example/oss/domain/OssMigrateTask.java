package com.example.oss.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("oss_migrate_task")
@AllArgsConstructor
@NoArgsConstructor
public class OssMigrateTask {

    @TableId(value = "task_id", type = IdType.ASSIGN_ID)
    private Long taskId;

    @TableField("source_config")
    private String sourceConfig;

    @TableField("target_config")
    private String targetConfig;

    @TableField("status")
    private String status;

    @TableField("total_count")
    private Integer totalCount;

    @TableField("success_count")
    private Integer successCount;

    @TableField("fail_count")
    private Integer failCount;

    @TableField("delete_source")
    private Boolean deleteSource;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("finished_at")
    private LocalDateTime finishedAt;

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_CANCELLED = "CANCELLED";
}