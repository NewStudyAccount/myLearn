CREATE TABLE IF NOT EXISTS `oss_migrate_task` (
    `task_id` BIGINT NOT NULL COMMENT '任务ID',
    `source_config` VARCHAR(100) NOT NULL COMMENT '源配置名称',
    `target_config` VARCHAR(100) NOT NULL COMMENT '目标配置名称',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态（PENDING/RUNNING/COMPLETED/FAILED/CANCELLED）',
    `total_count` INT NOT NULL DEFAULT 0 COMMENT '总文件数',
    `success_count` INT NOT NULL DEFAULT 0 COMMENT '成功数',
    `fail_count` INT NOT NULL DEFAULT 0 COMMENT '失败数',
    `delete_source` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除源端文件',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `finished_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    PRIMARY KEY (`task_id`),
    KEY `idx_status` (`status`),
    KEY `idx_source_config` (`source_config`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OSS迁移任务表';

CREATE TABLE IF NOT EXISTS `oss_migrate_task_item` (
    `item_id` BIGINT NOT NULL COMMENT '明细ID',
    `task_id` BIGINT NOT NULL COMMENT '关联任务ID',
    `oss_id` BIGINT NOT NULL COMMENT '关联文件ID',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '明细状态（PENDING/SUCCESS/FAILED）',
    `source_url` VARCHAR(255) NOT NULL COMMENT '源文件URL',
    `target_url` VARCHAR(255) DEFAULT NULL COMMENT '目标文件URL',
    `error_msg` TEXT DEFAULT NULL COMMENT '失败原因',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`item_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_oss_id` (`oss_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OSS迁移任务明细表';