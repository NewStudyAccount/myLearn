package com.example.oss.service;

import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OssFileService {


    public String uploadFile(MultipartFile file);
    public String uploadFile(MultipartFile file,String configName);

    public Long uploadFile(Long ossId,String fileName,String contentType,byte[] data);

    public String downloadFile(String fileName);

    public byte[] downloadFileContent(String fileUrl);

    TableDataInfo<SysOssFile> querySysOssFileListPage(SysOssFileQueryPageReq sysOssFileQueryPageReq);


    SysOssFile queryById(Long id);

    int deleteById(Long id);

    // 导出功能
    byte[] exportFile(Long ossId);
    InputStream exportFileStream(Long ossId);
    Map<String, byte[]> exportFiles(List<Long> ossIds);
    List<SysOssFile> exportFilesByCondition(String suffix, Date startTime, Date endTime);

    // 导入功能
    String importFile(MultipartFile file, String configName);
    List<Map<String, Object>> importFiles(List<MultipartFile> files, String configName);

    // 跨存储迁移
    SysOssFile migrateFile(Long ossId, String sourceConfig, String targetConfig);
    List<Map<String, Object>> migrateFiles(List<Long> ossIds, String sourceConfig, String targetConfig);
    List<Map<String, Object>> migrateAll(String sourceConfig, String targetConfig);
    Map<String, Object> getMigrateProgress(Long taskId);
}
