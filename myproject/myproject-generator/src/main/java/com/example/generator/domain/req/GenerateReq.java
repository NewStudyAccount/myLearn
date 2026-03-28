package com.example.generator.domain.req;

import lombok.Data;

@Data
public class GenerateReq {

    private String tableName;

    private String tablePrefix;

    private String entityPrefix;

    private Boolean generateEntity = true;

    private Boolean generateMapper = true;

    private Boolean generateService = true;

    private Boolean generateController = true;

    private Boolean generateFrontend = true;
}