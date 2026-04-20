package com.example.oss.domain.req.sysOssConfig;


import com.example.domain.PageQuery;
import com.example.oss.domain.SysOssConfig;
import lombok.Data;

@Data
public class SysOssConfigQueryPageReq extends SysOssConfig {


    private PageQuery pageQuery;

}
