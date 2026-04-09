## Why

myproject-oss 模块当前已有 Aliyun OSS 和 MinIO 的 Factory 实现，但缺失 S3 协议兼容的通用客户端工厂。项目父 POM 已声明 AWS S3 SDK 依赖（`software.amazon.awssdk:s3:2.31.78`）却未被使用。需要新增基于 S3 协议的 OSSClient 工厂，统一文件上传与下载能力，使模块支持任意兼容 S3 协议的对象存储服务（如 AWS S3、阿里云 OSS、MinIO 等）。

## What Changes

- 新增 `S3OssClientFactory` 实现类，使用 AWS S3 SDK v2 创建 `S3Client`
- 重构 `OssClientFactory` 接口，将 `uploadFile(byte[])` 方法扩展为支持上传与下载的完整文件操作接口
- 新增文件上传方法：`uploadFile(String objectName, byte[] data)`
- 新增文件下载方法：`downloadFile(String objectName) → byte[]`
- 在 `OssController` 中新增文件上传和下载 REST 端点
- 为 `AliyunOssClientFactory` 和 `MinioClientFactory` 补充上传下载实现

## Capabilities

### New Capabilities

- `s3-oss-client`: S3 协议兼容的 OSS 客户端工厂，提供文件上传与下载能力

### Modified Capabilities

- `oss-client-factory`: 扩展 OssClientFactory 接口，增加下载方法签名，统一上传方法参数

## Impact

- 影响模块：`myproject-oss`
- 影响已有类：`OssClientFactory`（接口变更）、`AliyunOssClientFactory`、`MinioClientFactory`、`OssController`
- 新增类：`S3OssClientFactory`
- 依赖：AWS S3 SDK v2（已声明，无需新增依赖）
