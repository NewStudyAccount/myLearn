package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * @TableName sys_oss_file
 */
@TableName(value ="sys_oss_file")
public class SysOssFile {
    /**
     * 
     */
    @TableId
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

    /**
     * 
     */
    public Long getOssId() {
        return ossId;
    }

    /**
     * 
     */
    public void setOssId(Long ossId) {
        this.ossId = ossId;
    }

    /**
     * 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * 
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * 
     */
    public String getFileSuffix() {
        return fileSuffix;
    }

    /**
     * 
     */
    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    /**
     * 
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysOssFile other = (SysOssFile) that;
        return (this.getOssId() == null ? other.getOssId() == null : this.getOssId().equals(other.getOssId()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getOriginalName() == null ? other.getOriginalName() == null : this.getOriginalName().equals(other.getOriginalName()))
            && (this.getFileSuffix() == null ? other.getFileSuffix() == null : this.getFileSuffix().equals(other.getFileSuffix()))
            && (this.getFileUrl() == null ? other.getFileUrl() == null : this.getFileUrl().equals(other.getFileUrl()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOssId() == null) ? 0 : getOssId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getOriginalName() == null) ? 0 : getOriginalName().hashCode());
        result = prime * result + ((getFileSuffix() == null) ? 0 : getFileSuffix().hashCode());
        result = prime * result + ((getFileUrl() == null) ? 0 : getFileUrl().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ossId=").append(ossId);
        sb.append(", fileName=").append(fileName);
        sb.append(", originalName=").append(originalName);
        sb.append(", fileSuffix=").append(fileSuffix);
        sb.append(", fileUrl=").append(fileUrl);
        sb.append("]");
        return sb.toString();
    }
}