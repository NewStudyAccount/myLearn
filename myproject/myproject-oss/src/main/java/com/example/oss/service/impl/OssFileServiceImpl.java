package com.example.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.PageQuery;
import com.example.domain.TableDataInfo;
import com.example.oss.domain.SysOssConfig;
import com.example.oss.domain.SysOssFile;
import com.example.oss.domain.req.sysOssFile.SysOssFileQueryPageReq;
import com.example.oss.factory.OssClientFactoryProvider;
import com.example.oss.mapper.OssFileMapper;
import com.example.oss.service.OssConfigService;
import com.example.oss.service.OssFileService;
import com.example.oss.service.OssMigrateService;
import com.example.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
public class OssFileServiceImpl extends ServiceImpl<OssFileMapper, SysOssFile> implements OssFileService {


    @Autowired
    private OssClientFactoryProvider ossClientFactoryProvider;

    @Autowired
    private OssConfigService ossConfigService;

    @Autowired
    private OssMigrateService ossMigrateService;

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
            ossClientFactoryProvider.getFactory(sysOssConfig).uploadFile(sysOssConfig,newFileName,contentType,file.getBytes());

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
        try {
            SysOssConfig sysOssConfig = ossConfigService.getByConfigName(configName);
            if (sysOssConfig == null) {
                throw new RuntimeException("OSS配置不存在: " + configName);
            }
            if (!Boolean.TRUE.equals(sysOssConfig.getIsActive())) {
                throw new RuntimeException("OSS配置未启用: " + configName);
            }

            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String newFileName = UUID.randomUUID().toString() + "." + split[1];

            String contentType = file.getContentType();
            ossClientFactoryProvider.getFactory(sysOssConfig).uploadFile(sysOssConfig, newFileName, contentType, file.getBytes());

            String endpoint = sysOssConfig.getEndpoint();
            String bucketName = sysOssConfig.getBucketName();
            String url = endpoint + "/" + bucketName + "/" + newFileName;
            SysOssFile sysOssFile = new SysOssFile(newFileName, originalFilename, split[1], url, contentType);

            insertSysOssFile(sysOssFile);

            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        ossClientFactoryProvider.getFactory(sysOssConfig).uploadFile(sysOssConfig,newFileName,contentType,data);

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
                return ossClientFactoryProvider.getFactory(sysOssConfig).downloadFile(sysOssConfig, objectName);
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

    // ==================== 导出功能 ====================

    @Override
    public byte[] exportFile(Long ossId) {
        SysOssFile sysOssFile = this.baseMapper.selectById(ossId);
        if (sysOssFile == null) {
            throw new RuntimeException("文件不存在: " + ossId);
        }
        return downloadFileContent(sysOssFile.getFileUrl());
    }

    @Override
    public InputStream exportFileStream(Long ossId) {
        SysOssFile sysOssFile = this.baseMapper.selectById(ossId);
        if (sysOssFile == null) {
            throw new RuntimeException("文件不存在: " + ossId);
        }
        return downloadFileStreamByUrl(sysOssFile.getFileUrl());
    }

    @Override
    public Map<String, byte[]> exportFiles(List<Long> ossIds) {
        Map<String, byte[]> result = new HashMap<>();
        for (Long ossId : ossIds) {
            try {
                byte[] data = exportFile(ossId);
                SysOssFile sysOssFile = this.baseMapper.selectById(ossId);
                result.put(sysOssFile.getOriginalName(), data);
            } catch (Exception e) {
                result.put("FAILED_" + ossId, null);
            }
        }
        return result;
    }

    @Override
    public List<SysOssFile> exportFilesByCondition(String suffix, Date startTime, Date endTime) {
        LambdaQueryWrapper<SysOssFile> wrapper = new LambdaQueryWrapper<>();
        if (suffix != null && !suffix.isEmpty()) {
            wrapper.eq(SysOssFile::getFileSuffix, suffix);
        }
        if (startTime != null) {
            wrapper.ge(SysOssFile::getOssId, SnowflakeIdUtil.ossNextId()); // 使用ID作为时间近似
        }
        return this.baseMapper.selectList(wrapper);
    }

    // ==================== 导入功能 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importFile(MultipartFile file, String configName) {
        SysOssConfig sysOssConfig = ossConfigService.getByConfigName(configName);
        if (sysOssConfig == null) {
            throw new RuntimeException("OSS配置不存在: " + configName);
        }
        if (!Boolean.TRUE.equals(sysOssConfig.getIsActive())) {
            throw new RuntimeException("OSS配置未启用: " + configName);
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String newFileName = UUID.randomUUID().toString() + "." + split[1];

            String contentType = file.getContentType();
            ossClientFactoryProvider.getFactory(sysOssConfig).uploadFile(sysOssConfig, newFileName, contentType, file.getBytes());

            String endpoint = sysOssConfig.getEndpoint();
            String bucketName = sysOssConfig.getBucketName();
            String url = endpoint + "/" + bucketName + "/" + newFileName;
            SysOssFile sysOssFile = new SysOssFile(newFileName, originalFilename, split[1], url, contentType);

            insertSysOssFile(sysOssFile);
            return url;
        } catch (IOException e) {
            throw new RuntimeException("导入文件失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> importFiles(List<MultipartFile> files, String configName) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", file.getOriginalFilename());
            try {
                String url = importFile(file, configName);
                result.put("success", true);
                result.put("url", url);
            } catch (Exception e) {
                result.put("success", false);
                result.put("error", e.getMessage());
            }
            results.add(result);
        }
        return results;
    }

    // ==================== 跨存储迁移 ====================

    @Override
    public SysOssFile migrateFile(Long ossId, String sourceConfig, String targetConfig) {
        Long taskId = ossMigrateService.createTask(sourceConfig, targetConfig, List.of(ossId), false);
        ossMigrateService.executeAsync(taskId);
        waitForCompletion(taskId);
        return this.baseMapper.selectById(ossId);
    }

    @Override
    public List<Map<String, Object>> migrateFiles(List<Long> ossIds, String sourceConfig, String targetConfig) {
        Long taskId = ossMigrateService.createTask(sourceConfig, targetConfig, ossIds, false);
        ossMigrateService.executeAsync(taskId);
        waitForCompletion(taskId);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Long ossId : ossIds) {
            Map<String, Object> result = new HashMap<>();
            result.put("ossId", ossId);
            result.put("taskId", taskId);
            SysOssFile file = this.baseMapper.selectById(ossId);
            if (file != null) {
                result.put("success", true);
                result.put("newUrl", file.getFileUrl());
            } else {
                result.put("success", false);
            }
            results.add(result);
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> migrateAll(String sourceConfig, String targetConfig) {
        Long taskId = ossMigrateService.createTask(sourceConfig, targetConfig, null, false);
        ossMigrateService.executeAsync(taskId);
        waitForCompletion(taskId);

        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> finalProgress = ossMigrateService.getProgress(taskId);
        results.add(finalProgress);
        return results;
    }

    private void waitForCompletion(Long taskId) {
        Map<String, Object> progress = ossMigrateService.getProgress(taskId);
        while (!isTerminal(progress)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("迁移被中断");
            }
            progress = ossMigrateService.getProgress(taskId);
        }
    }

    private boolean isTerminal(Map<String, Object> progress) {
        String status = (String) progress.get("status");
        return "COMPLETED".equals(status) || "FAILED".equals(status) || "CANCELLED".equals(status);
    }

    @Override
    public Map<String, Object> getMigrateProgress(Long taskId) {
        return ossMigrateService.getProgress(taskId);
    }

    // ==================== 私有辅助方法 ====================

    private InputStream downloadFileStreamByUrl(String fileUrl) {
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
            return ossClientFactoryProvider.getFactory(sysOssConfig).downloadFileStream(sysOssConfig, objectName);
        } else {
            throw new RuntimeException("无效的文件URL: " + fileUrl);
        }
    }

    private SysOssConfig getConfigByName(String configName) {
        SysOssConfig config = ossConfigService.getByConfigName(configName);
        if (config == null) {
            throw new RuntimeException("OSS配置不存在: " + configName);
        }
        if (!Boolean.TRUE.equals(config.getIsActive())) {
            throw new RuntimeException("OSS配置未启用: " + configName);
        }
        return config;
    }
}