package com.example.domain.req.sysUser;

import com.example.domain.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class SysUserQueryReq {


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("USER_ID")
    private Long userId;
}
