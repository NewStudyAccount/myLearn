package com.example.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssFileService {


    public String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file,String configName);

    public String downloadFile(String fileName);



}
