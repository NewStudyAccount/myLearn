package com.example.domain.req;

import com.example.domain.PageQuery;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SysTagQueryPageReq {

    @JsonProperty("PAGE_QUERY")
    private PageQuery pageQuery;
}
