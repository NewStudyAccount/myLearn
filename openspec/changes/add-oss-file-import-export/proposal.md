## Why

myproject-oss 模块当前仅支持单向文件上传和下载，缺乏批量导入/导出能力，也不支持在不同对象存储后端之间迁移数据。当需要备份 OSS 数据、在不同存储提供商之间切换、或将本地文件批量导入 OSS 时，必须依赖手动操作或外部脚本，效率低且容易出错。

## What Changes

- 新增 OSS 文件批量导出功能：支持按文件列表或条件筛选将 OSS 文件下载到本地
- 新增 OSS 文件批量导入功能：支持将本地目录或文件批量上传到指定 OSS 后端
- 新增跨存储数据迁移功能：支持在不同 OSS 配置（如 MinIO → 阿里云 OSS、S3 → MinIO）之间直接传输文件，无需经过本地中转
- 完善现有 `uploadFile(MultipartFile, String configName)` 存根方法，支持指定配置名上传

## Capabilities

### New Capabilities
- `oss-file-export`: OSS 文件导出能力，支持单文件和批量文件从对象存储导出到本地
- `oss-file-import`: OSS 文件导入能力，支持本地文件批量导入到对象存储
- `oss-cross-storage-migrate`: 跨对象存储数据迁移能力，支持在不同 OSS 后端之间传输文件

### Modified Capabilities
- `editor-file-upload`: 现有文件上传功能需要支持指定配置名（当前为存根）

## Impact

- 后端模块：myproject-oss（主要变更）
- 新增依赖：可能需要引入文件流处理相关工具类
- API 变更：OssController 新增批量导入/导出/迁移端点
- 数据库：sys_oss_file 表可能需要新增字段记录文件来源/迁移状态
- 前端：需要新增导入/导出/迁移管理界面
