## 1. 重构 OssClientFactory 接口

- [x] 1.1 修改 `OssClientFactory` 接口：将 `uploadFile(byte[])` 替换为 `uploadFile(String objectName, byte[] data)`
- [x] 1.2 在 `OssClientFactory` 接口中新增 `downloadFile(String objectName)` 方法，返回 `byte[]`

## 2. 补全已有 Factory 实现

- [x] 2.1 实现 `AliyunOssClientFactory.uploadFile()`：使用 `OSS.putObject()` 上传文件
- [x] 2.2 实现 `AliyunOssClientFactory.downloadFile()`：使用 `OSS.getObject()` 下载文件并返回字节数组
- [x] 2.3 实现 `MinioClientFactory.uploadFile()`：使用 `MinioClient.putObject()` 上传文件
- [x] 2.4 实现 `MinioClientFactory.downloadFile()`：使用 `MinioClient.getObject()` 下载文件并返回字节数组

## 3. 新增 S3OssClientFactory

- [x] 3.1 创建 `S3OssClientFactory` 类，实现 `OssClientFactory` 接口，provider 为 `"s3"`
- [x] 3.2 实现 `createClient()`：使用 `S3Client.builder()` 配置 endpoint、credentials、region 构建 S3Client
- [x] 3.3 实现 `uploadFile()`：使用 `S3Client.putObject()` 上传文件
- [x] 3.4 实现 `downloadFile()`：使用 `S3Client.getObject()` 下载文件并返回字节数组
- [x] 3.5 支持 path-style access 等扩展配置（通过 extraConfig JSON 解析）

## 4. 更新 OssClientServiceImpl

- [x] 4.1 在 `testConnection()` 方法中增加 `S3Client` 类型的连接测试（调用 `listBuckets()`）

## 5. 新增 Controller 端点

- [x] 5.1 在 `OssController` 中新增 `POST /project/oss/upload` 端点：接收 MultipartFile 和 configName 参数
- [x] 5.2 在 `OssController` 中新增 `GET /project/oss/download` 端点：接收 configName 和 objectName 参数
- [x] 5.3 注入 `OssConfigService` 和 `OssClientFactoryProvider`，在端点中根据 configName 获取 Factory 并执行上传/下载
