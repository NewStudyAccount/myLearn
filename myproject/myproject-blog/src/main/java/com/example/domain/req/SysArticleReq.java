package com.example.domain.req;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysArticleReq {

    /**
     * 文章名
     */
    private String title;

    /**
     * 预览图
     */
    private String cover;

}
