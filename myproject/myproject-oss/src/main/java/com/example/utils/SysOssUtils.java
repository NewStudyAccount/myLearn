package com.example.utils;

import java.util.UUID;

public class SysOssUtils {


    public static String genNewFileName(String originalName){
        return UUID.randomUUID().toString();
    }

    public static String genUrl(){
        return "https://oss.aliyuncs.com";

    }

}
