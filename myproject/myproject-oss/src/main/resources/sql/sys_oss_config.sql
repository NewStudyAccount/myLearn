-- OSS配置表
CREATE TABLE IF NOT EXISTS `sys_oss_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称（唯一标识）',
    `provider` VARCHAR(50) NOT NULL COMMENT '提供商类型（aliyun、minio等）',
    `endpoint` VARCHAR(255) NOT NULL COMMENT '服务端点',
    `access_key` VARCHAR(255) NOT NULL COMMENT '访问密钥',
    `secret_key` VARCHAR(255) NOT NULL COMMENT '秘密密钥',
    `bucket_name` VARCHAR(100) NOT NULL COMMENT '存储桶名称',
    `region` VARCHAR(50) DEFAULT NULL COMMENT '区域（可选）',
    `extra_config` TEXT DEFAULT NULL COMMENT 'JSON格式的扩展配置',
    `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1启用，0禁用）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_name` (`config_name`),
    KEY `idx_provider` (`provider`),
    KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OSS配置表';

-- 插入示例数据
INSERT INTO `sys_oss_config` (`config_name`, `provider`, `endpoint`, `access_key`, `secret_key`, `bucket_name`, `region`, `extra_config`, `is_active`) VALUES
('aliyun-default', 'aliyun', 'https://oss-cn-hangzhou.aliyuncs.com', 'your-access-key-id', 'your-access-key-secret', 'my-bucket', 'cn-hangzhou', NULL, 1),
('minio-local', 'minio', 'http://localhost:9000', 'minioadmin', 'minioadmin', 'mybucket', NULL, '{"pathStyleAccess": true}', 1);