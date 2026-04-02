package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.config.OssClient;
import com.example.domain.SysOssFile;
import com.example.mapper.SysOssFileMapper;
import com.example.oss.facade.OssClientFacade;
import com.example.service.SysOssFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @description 针对表【sys_oss_file】的数据库操作Service实现
 * @createDate 2025-07-19 16:06:21
 */
@Service
public class SysOssFileServiceImpl extends ServiceImpl<SysOssFileMapper, SysOssFile>
    implements SysOssFileService {


    @Autowired
    private SysOssFileMapper sysOssFileMapper;

//    @Autowired
//    private OssService ossService;

    @Autowired
    private OssClientFacade ossClientFacade;

    @Override
    public String uploadFile(MultipartFile file) {

        String url = "";
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String newFileName = UUID.randomUUID().toString() + "."+split[1];

            OssClient defaultOssClient = (OssClient) ossClientFacade.getDefaultClient();
            url = defaultOssClient.uploadFile(newFileName, inputStream);


            SysOssFile sysOssFile = new SysOssFile(newFileName,originalFilename,split[1],url);

            insertSysOssFile(sysOssFile);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    @Override
    public String uploadBigFile(MultipartFile file) {
        String url = "";
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String newFileName = UUID.randomUUID().toString() + "."+split[1];

            OssClient defaultOssClient = (OssClient) ossClientFacade.getDefaultClient();
            url = defaultOssClient.uploadBigFileFromStream(newFileName, inputStream);


            SysOssFile sysOssFile = new SysOssFile(newFileName,originalFilename,split[1],url);

            insertSysOssFile(sysOssFile);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    @Override
    public int insertSysOssFile(SysOssFile sysOssFile) {
        return sysOssFileMapper.insert(sysOssFile);
    }
}