package com.example.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName sys_oss_config
 */
@Data
@TableName(value ="sys_oss_config")
public class SysOssConfig {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String configName;

    /**
     * 
     */
    private String bucketName;

    /**
     * 
     */
    private String accessKey;

    /**
     * 
     */
    private String keySecret;

    /**
     * 
     */
    private String endPoint;

    /**
     * 
     */
    private String region;

    /**
     * 
     */
    private Integer status;

}