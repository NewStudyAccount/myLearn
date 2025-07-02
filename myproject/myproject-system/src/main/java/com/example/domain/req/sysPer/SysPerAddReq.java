package com.example.domain.req.sysPer;

import lombok.Data;

/**
 * 缺陷表
 * @TableName sys_per
 */
@Data
public class SysPerAddReq {

    /**
     * 权限code
     */
    private String perCode;

}