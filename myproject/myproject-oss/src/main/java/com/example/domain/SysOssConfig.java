package com.example.domain;

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
    private Long id;

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


    private String fileFolder;

    /**
     * 是否默认（0=是,1=否）
     */
    private Integer status;

}