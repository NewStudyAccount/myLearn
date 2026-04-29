package com.example.oss.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.PageQuery;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import com.example.oss.factory.OssClientFactory;
import com.example.oss.mapper.OssFileMapper;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssFileService;
import com.example.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
public class OssFileServiceImpl extends ServiceImpl<OssFileMapper, SysOssFile> implements OssFileService {


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

            List<SysOssConfig> sysOssConfigs = ossConfigService.listActive();
            if (CollectionUtils.isEmpty(sysOssConfigs)) {
                throw new  RuntimeException("未找到有效的OSS配置");
            }

            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String newFileName = UUID.randomUUID().toString() + "."+split[1];

            SysOssConfig sysOssConfig = ossConfigService.getByConfigName("minio-local");
            String contentType = file.getContentType();
            ossClientFactory.uploadFile(sysOssConfig,newFileName,contentType,file.getBytes());

            String endpoint = sysOssConfig.getEndpoint();
            String bucketName = sysOssConfig.getBucketName();
//            http://192.168.99.100:9000/my-bucket/62237aa2-b510-4acf-9c5e-32a94e953540.png
            url = endpoint+"/"+bucketName+"/"+newFileName;
            SysOssFile sysOssFile = new SysOssFile(newFileName,originalFilename,split[1],url,contentType);

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
    @Transactional(rollbackFor = Exception.class)
    public Long uploadFile(Long ossId,String fileName,String contentType,byte[] data) {
        String url = "";

        long ossNextId = SnowflakeIdUtil.ossNextId();
        List<SysOssConfig> sysOssConfigs = ossConfigService.listActive();
        if (CollectionUtils.isEmpty(sysOssConfigs)) {
            throw new  RuntimeException("未找到有效的OSS配置");
        }

        String[] split = fileName.split("\\.");
        String newFileName = UUID.randomUUID().toString() + "."+split[1];
        if (ossId!= null) {
            SysOssFile sysOssFile = this.baseMapper.selectById(ossId);
            newFileName = sysOssFile.getFileName();
            ossNextId = sysOssFile.getOssId();
        }


        SysOssConfig sysOssConfig = ossConfigService.getByConfigName("minio-local");
        String endpoint = sysOssConfig.getEndpoint();
        String bucketName = sysOssConfig.getBucketName();
        url = endpoint+"/"+bucketName+"/"+newFileName;

        //执行上传文件
        ossClientFactory.uploadFile(sysOssConfig,newFileName,contentType,data);

        SysOssFile sysOssFile = new SysOssFile(ossNextId,newFileName,fileName,split[1],url,contentType);

        if (ossId == null){
            insertSysOssFile(sysOssFile);
        }else {
            this.baseMapper.updateById(sysOssFile);
        }

        return ossNextId;
    }

    @Override
    public String downloadFile(String fileName) {
        return "";
    }

    @Override
    public byte[] downloadFileContent(String fileUrl) {
        try {
            List<SysOssConfig> sysOssConfigs = ossConfigService.listActive();
            if (CollectionUtils.isEmpty(sysOssConfigs)) {
                throw new RuntimeException("未找到有效的OSS配置");
            }

            SysOssConfig sysOssConfig = ossConfigService.getByConfigName("minio-local");
            if (sysOssConfig == null) {
                sysOssConfig = sysOssConfigs.get(0);
            }

            String endpoint = sysOssConfig.getEndpoint();
            String bucketName = sysOssConfig.getBucketName();

            String prefix = endpoint + "/" + bucketName + "/";
            if (fileUrl.startsWith(prefix)) {
                String objectName = fileUrl.substring(prefix.length());
                return ossClientFactory.downloadFile(sysOssConfig, objectName);
            } else {
                throw new RuntimeException("无效的文件URL: " + fileUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("下载文件内容失败: " + e.getMessage(), e);
        }
    }

    @Override
    public TableDataInfo<SysOssFile> querySysOssFileListPage(SysOssFileQueryPageReq sysOssFileQueryPageReq) {
        PageQuery pageQuery = sysOssFileQueryPageReq.getPageQuery();
        Page<SysOssFile> sysOssFilePage = this.baseMapper.selectPage(pageQuery.build(), null);
        return TableDataInfo.build(sysOssFilePage);

    }

    @Override
    public SysOssFile queryById(Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public int deleteById(Long id) {
        return this.baseMapper.deleteById(id);
    }


    public void insertSysOssFile(SysOssFile sysOssFile) {
        long ossNextId = SnowflakeIdUtil.ossNextId();
        sysOssFile.setOssId(ossNextId);
        this.baseMapper.insert(sysOssFile);
    }

}
