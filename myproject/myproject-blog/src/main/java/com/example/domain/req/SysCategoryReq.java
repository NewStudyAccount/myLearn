package com.example.domain.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章分类
 * @TableName sys_category
 */
@Data
public class SysCategoryReq{

    /**
     * 分类名
     */
    private String name;

}