package com.example.domain.req;

import lombok.Data;

@Data
public class GenerateReq {

    private String tableName;

    private String tablePrefix;

    private String packageName;


}
