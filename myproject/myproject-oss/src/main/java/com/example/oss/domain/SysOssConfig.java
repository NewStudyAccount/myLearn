package com.example.oss.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * OSS配置实体类
 */
@Data
@TableName("sys_oss_config")
public class SysOssConfig {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置名称（唯一标识）
     */
    @TableField("config_name")
    private String configName;

    /**
     * 提供商类型（aliyun、minio等）
     */
    @TableField("provider")
    private String provider;

    /**
     * 服务端点
     */
    @TableField("endpoint")
    private String endpoint;

    /**
     * 访问密钥
     */
    @TableField("access_key")
    private String accessKey;

    /**
     * 秘密密钥
     */
    @TableField("secret_key")
    private String secretKey;

    /**
     * 存储桶名称
     */
    @TableField("bucket_name")
    private String bucketName;

    /**
     * 区域（可选）
     */
    @TableField("region")
    private String region;

    /**
     * JSON格式的扩展配置
     */
    @TableField("extra_config")
    private String extraConfig;

    /**
     * 是否启用（1启用，0禁用）
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 创建时间
     */
    @TableField("created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}