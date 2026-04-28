
package com.example.domain.req;


import com.example.domain.PageQuery;
import com.example.domain.pojo.SysArticleContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 文章内容分页查询请求对象
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysArticleContentQueryPageReq extends SysArticleContent {

/**
* 分页参数
*/
private PageQuery pageQuery;
}