package com.example.oss.service;

import com.example.domain.Response;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface OssFileService {


    public String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file,String configName);

    public String downloadFile(String fileName);


    TableDataInfo<SysOssFile> querySysOssFileListPage(SysOssFileQueryPageReq sysOssFileQueryPageReq);


    SysOssFile queryById(Long id);

    int deleteById(Long id);




}
