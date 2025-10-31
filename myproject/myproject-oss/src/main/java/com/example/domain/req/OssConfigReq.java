package com.example.domain.req;

import lombok.Data;

@Data
public class OssConfigReq {

    private String configName;


    private String bucketName;


    private String accessKey;


    private String keySecret;


    private String endPoint;


    private String region;


    private String fileFolder;


    private Integer status;
}
