package com.example.domain.req;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class SysArticleReq {


    private Long id;

    /**
     * 文章名
     */
    private String title;

    /**
     * 预览图
     */
    private String cover;

    //标签
    private List<Long> tagIds;

    //分类
    private Long categoryId;

    private String content;

}
