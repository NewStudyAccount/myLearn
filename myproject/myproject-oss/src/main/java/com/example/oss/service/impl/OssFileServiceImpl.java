package com.example.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oss.domain.OssConfig;
import com.example.oss.domain.OssFile;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.mapper.OssConfigMapper;
import com.example.oss.mapper.OssFileMapper;
import com.example.oss.service.OssClientService;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
public class OssFileServiceImpl extends ServiceImpl<OssFileMapper, OssFile> implements OssFileService {


    @Autowired
    private OssClientFactory ossClientFactory;

    @Autowired
    private OssConfigService ossConfigService;

    public String newFileName() {
        return "";
    }


    @Override
    public String uploadFile(MultipartFile file) {
        String url = "";
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String newFileName = UUID.randomUUID().toString() + "."+split[1];

            OssConfig ossConfig = ossConfigService.getByConfigName("minio-local");
            url = ossClientFactory.uploadFile(ossConfig,newFileName,file.getBytes());


            OssFile sysOssFile = new OssFile(newFileName,originalFilename,split[1],url);

            insertSysOssFile(sysOssFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return url;
    }

    @Override
    public String uploadFile(MultipartFile file, String configName) {
        return "";
    }

    @Override
    public String downloadFile(String fileName) {
        return "";
    }


    public void insertSysOssFile(OssFile sysOssFile) {
        this.baseMapper.insert(sysOssFile);
    }

}
