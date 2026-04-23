package com.example.oss.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName sys_oss_file
 */
@Data
@TableName(value ="sys_oss_file")
@AllArgsConstructor
@NoArgsConstructor
public class SysOssFile {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long ossId;

    /**
     * 
     */
    private String fileName;

    /**
     * 
     */
    private String originalName;

    /**
     * 
     */
    private String fileSuffix;

    /**
     * 
     */
    private String fileUrl;


    private String contentType;

    public SysOssFile(String fileName, String originalName, String fileSuffix, String fileUrl, String contentType) {
        this.fileName = fileName;
        this.originalName = originalName;
        this.fileSuffix = fileSuffix;
        this.fileUrl = fileUrl;
        this.contentType = contentType;
    }
}