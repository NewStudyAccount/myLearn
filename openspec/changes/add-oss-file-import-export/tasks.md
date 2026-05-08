## 1. 基础设施完善

- [x] 1.1 完善 OssFileServiceImpl 中的 uploadFile(MultipartFile, String configName) 存根方法
- [x] 1.2 在 OssClientFactory 接口中新增 deleteFile 方法用于删除存储中的文件
- [x] 1.3 在 OssClientFactory 接口中新增 listFiles 方法用于列举存储中的文件对象

## 2. 文件导出功能

- [x] 2.1 在 OssFileService 中新增 exportFile(Long ossId) 方法，返回文件字节数组
- [x] 2.2 在 OssFileService 中新增 exportFiles(List<Long> ossIds) 方法，返回 ZIP 压缩流
- [x] 2.3 在 OssFileService 中新增 exportFilesByCondition(String suffix, Date startTime, Date endTime) 方法
- [x] 2.4 在 OssController 中新增 GET /export/{id} 端点，单文件导出
- [x] 2.5 在 OssController 中新增 POST /export/batch 端点，批量文件导出为 ZIP
- [x] 2.6 在 OssController 中新增 POST /export/condition 端点，按条件筛选导出

## 3. 文件导入功能

- [x] 3.1 在 OssFileService 中新增 importFile(MultipartFile file, String configName) 方法
- [x] 3.2 在 OssFileService 中新增 importFiles(List<MultipartFile> files, String configName) 方法
- [x] 3.3 在 OssController 中新增 POST /import/file 端点，单文件导入
- [x] 3.4 在 OssController 中新增 POST /import/batch 端点，批量文件导入
- [ ] 3.5 在 OssController 中新增 POST /import/excel 端点，通过 Excel 清单导入

## 4. 跨存储迁移功能

- [x] 4.1 在 OssFileService 中新增 migrateFile(Long ossId, String sourceConfig, String targetConfig) 方法
- [x] 4.2 在 OssFileService 中新增 migrateFiles(List<Long> ossIds, String sourceConfig, String targetConfig) 方法
- [x] 4.3 在 OssFileService 中新增 migrateAll(String sourceConfig, String targetConfig) 全量迁移方法
- [ ] 4.4 创建 OssMigrateTask 实体类，记录迁移任务状态和进度
- [ ] 4.5 创建 OssMigrateTaskMapper 和对应数据库表 oss_migrate_task
- [x] 4.6 在 OssFileService 中新增 getMigrateProgress(Long taskId) 方法
- [x] 4.7 在 OssController 中新增 POST /migrate/file 端点，单文件迁移
- [x] 4.8 在 OssController 中新增 POST /migrate/batch 端点，批量迁移
- [x] 4.9 在 OssController 中新增 POST /migrate/all 端点，全量迁移
- [x] 4.10 在 OssController 中新增 GET /migrate/progress/{taskId} 端点，查询迁移进度

## 5. 前端界面

- [x] 5.1 创建文件导出页面，支持单文件下载和批量导出
- [x] 5.2 创建文件导入页面，支持文件上传和 Excel 清单导入
- [x] 5.3 创建跨存储迁移页面，支持选择源/目标配置和迁移操作
- [x] 5.4 创建迁移任务进度查看页面
- [x] 5.5 在路由配置中添加新页面路由
