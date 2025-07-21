package com.example.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OssConfigUpdateReq {

    @NotNull(message = "配置项id不能为空")
    private Integer id;

    @NotBlank(message = "配置项名称不能为空")
    private String configName;


    private String bucketName;


    private String accessKey;


    private String keySecret;


    private String endPoint;


    private String region;


    private String fileFolder;


    private Integer status;
}
