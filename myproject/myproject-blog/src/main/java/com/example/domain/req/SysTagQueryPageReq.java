package com.example.domain.req;

import com.example.domain.PageQuery;
import com.example.domain.pojo.SysTag;
import lombok.Data;

@Data
public class SysTagQueryPageReq extends SysTag {

    private PageQuery pageQuery;
}
