package com.example.domain.req.sysUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class SysUserQueryReq {


    @JsonProperty("userId")
    private Long userId;
}
