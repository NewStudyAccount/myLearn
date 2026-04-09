## Context

myproject-oss 模块采用工厂模式管理多种对象存储客户端。当前已实现 Aliyun OSS 和 MinIO 两种 Factory，但父 POM 中声明的 AWS S3 SDK（`software.amazon.awssdk:s3:2.31.78`）尚未被使用。同时，`OssClientFactory` 接口的 `uploadFile(byte[])` 方法签名过于简单，缺少 objectName 参数和 download 方法。各 Factory 实现也不完整（Aliyun 缺少 uploadFile 实现，MinIO 的是空壳）。

本次改造目标：新增 S3 协议兼容的客户端工厂，重构接口以支持完整的文件上传下载能力，同时补全已有 Factory 的实现。

## Goals / Non-Goals

**Goals:**
- 新增 `S3OssClientFactory`，使用 AWS S3 SDK v2 创建 `S3Client`
- 扩展 `OssClientFactory` 接口，统一文件操作方法签名
- 提供 `uploadFile(objectName, data)` 和 `downloadFile(objectName)` 方法
- 补全 `AliyunOssClientFactory` 和 `MinioClientFactory` 的上传下载实现
- 在 `OssController` 中暴露文件上传下载 REST 端点
- `OssClientServiceImpl.testConnection` 支持 S3Client 类型

**Non-Goals:**
- 不改变现有的配置缓存机制（Redis + MySQL）
- 不改变现有 Factory 的 `createClient` 逻辑
- 不引入新的 Maven 依赖（S3 SDK 已声明）
- 不实现分片上传、断点续传等高级特性

## Decisions

### 1. 重构 OssClientFactory 接口

将 `uploadFile(byte[])` 替换为完整的方法签名：
```java
String uploadFile(String objectName, byte[] data);
byte[] downloadFile(String objectName);
```
**理由：** 上传需要指定对象名，下载需要返回字节数组。原接口 `uploadFile(byte[])` 缺少 objectName，无法实际使用。

**替代方案考虑：**
- 新增单独的 `OssFileOperation` 接口 → 增加了复杂度，Factory 本应负责客户端的完整生命周期
- 使用泛型 `T` 替代 `Object` → 对现有 Factory 改动过大

### 2. S3OssClient 使用 AWS S3 SDK v2

使用 `software.amazon.awssdk.services.s3.S3Client`，通过 `S3Client.builder()` 配置 endpoint、credentials、region。
**理由：** S3 SDK 已在父 POM 中声明版本管理，且 S3 协议是事实标准，兼容 MinIO、阿里云 OSS 等。

### 3. 文件上传下载直接通过 Factory 方法操作

每个 Factory 实现使用其对应 SDK 执行上传下载操作，不引入额外的 Service 层抽象。
**理由：** 操作逻辑简单，工厂已持有客户端创建能力，直接在工厂方法中操作避免不必要的中间层。

### 4. Controller 端点设计

```
POST   /project/oss/upload?configName=xxx     - 上传文件（MultipartFile）
GET    /project/oss/download?configName=xxx&objectName=yyy - 下载文件
```
通过 `configName` 参数动态选择使用哪个 OSS 配置。

## Risks / Trade-offs

- **[Risk]** Factory 接口变更是 breaking change，现有实现必须同步修改 → **Mitigation：** Aliyun 和 MinIO Factory 同步更新，编译期即可发现遗漏
- **[Risk]** S3Client 不同 provider 的 endpoint URL 格式差异（如阿里云 OSS 的虚拟托管 vs MinIO 的路径风格） → **Mitigation：** 支持通过 extraConfig JSON 字段配置 pathStyleAccess 等参数
- **[Risk]** 大文件下载可能导致内存溢出 → **Mitigation：** 本次不处理大文件场景，后续可引入流式下载
