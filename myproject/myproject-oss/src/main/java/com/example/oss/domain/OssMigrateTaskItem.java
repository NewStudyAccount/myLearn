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
@TableName("oss_migrate_task_item")
@AllArgsConstructor
@NoArgsConstructor
public class OssMigrateTaskItem {

    @TableId(value = "item_id", type = IdType.ASSIGN_ID)
    private Long itemId;

    @TableField("task_id")
    private Long taskId;

    @TableField("oss_id")
    private Long ossId;

    @TableField("status")
    private String status;

    @TableField("source_url")
    private String sourceUrl;

    @TableField("target_url")
    private String targetUrl;

    @TableField("error_msg")
    private String errorMsg;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
}