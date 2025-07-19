package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * @TableName sys_oss_config
 */
@TableName(value ="sys_oss_config")
public class SysOssConfig {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String configName;

    /**
     * 
     */
    private String bucketName;

    /**
     * 
     */
    private String accessKey;

    /**
     * 
     */
    private String keySecret;

    /**
     * 
     */
    private String endPoint;

    /**
     * 
     */
    private String region;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * 
     */
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
     * 
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * 
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * 
     */
    public String getKeySecret() {
        return keySecret;
    }

    /**
     * 
     */
    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }

    /**
     * 
     */
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * 
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * 
     */
    public String getRegion() {
        return region;
    }

    /**
     * 
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     */
    public void setStatus(Integer status) {
        this.status = status;
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
        SysOssConfig other = (SysOssConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getConfigName() == null ? other.getConfigName() == null : this.getConfigName().equals(other.getConfigName()))
            && (this.getBucketName() == null ? other.getBucketName() == null : this.getBucketName().equals(other.getBucketName()))
            && (this.getAccessKey() == null ? other.getAccessKey() == null : this.getAccessKey().equals(other.getAccessKey()))
            && (this.getKeySecret() == null ? other.getKeySecret() == null : this.getKeySecret().equals(other.getKeySecret()))
            && (this.getEndPoint() == null ? other.getEndPoint() == null : this.getEndPoint().equals(other.getEndPoint()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getConfigName() == null) ? 0 : getConfigName().hashCode());
        result = prime * result + ((getBucketName() == null) ? 0 : getBucketName().hashCode());
        result = prime * result + ((getAccessKey() == null) ? 0 : getAccessKey().hashCode());
        result = prime * result + ((getKeySecret() == null) ? 0 : getKeySecret().hashCode());
        result = prime * result + ((getEndPoint() == null) ? 0 : getEndPoint().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", configName=").append(configName);
        sb.append(", bucketName=").append(bucketName);
        sb.append(", accessKey=").append(accessKey);
        sb.append(", keySecret=").append(keySecret);
        sb.append(", endPoint=").append(endPoint);
        sb.append(", region=").append(region);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}