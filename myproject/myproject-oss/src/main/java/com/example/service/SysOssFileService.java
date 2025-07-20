package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.SysOssFile;
import org.springframework.web.multipart.MultipartFile;

/**
* @author AI
* @description 针对表【sys_oss_file】的数据库操作Service
* @createDate 2025-07-19 16:06:21
*/
public interface SysOssFileService extends IService<SysOssFile> {

    public int uploadFile(MultipartFile file);

    public int insertSysOssFile(SysOssFile sysOssFile);


}
