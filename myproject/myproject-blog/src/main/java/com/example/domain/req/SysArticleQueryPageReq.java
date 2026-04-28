
package com.example.domain.req;

import com.example.domain.PageQuery;
import com.example.domain.pojo.SysArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 文章表分页查询请求对象
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysArticleQueryPageReq extends SysArticle {

    /**
    * 分页参数
    */
    private PageQuery pageQuery;
}