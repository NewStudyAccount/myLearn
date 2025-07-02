package com.example.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 标签
 * @TableName sys_tag
 */
@Data
public class SysTagReq {

    /**
     * 标签名
     */
    private String name;
}