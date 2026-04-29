package com.example.oss.service;

import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import org.springframework.web.multipart.MultipartFile;

public interface OssFileService {


    public String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file,String configName);

    public Long uploadFile(Long ossId,String fileName,String contentType,byte[] data);

    public String downloadFile(String fileName);

    public byte[] downloadFileContent(String fileUrl);

    TableDataInfo<SysOssFile> querySysOssFileListPage(SysOssFileQueryPageReq sysOssFileQueryPageReq);


    SysOssFile queryById(Long id);

    int deleteById(Long id);




}
