## Why

当前代码生成器只能在线预览生成的代码，用户需要手动复制代码到项目中，操作繁琐且容易出错。增加文件下载功能可以将生成的代码打包成ZIP文件一键下载，提升用户体验。

## What Changes

- 新增ZIP打包下载功能，支持一键下载所有生成的代码文件
- 支持按模块选择下载（后端代码、前端代码、Mapper XML）
- 下载时自动生成包含正确包路径的目录结构
- 支持生成代码的在线预览和单文件下载

## Capabilities

### New Capabilities

- `generator-download`: 代码生成器的文件下载能力，支持ZIP打包和单文件下载

### Modified Capabilities

- (无)

## Impact

- 后端：新增下载接口和ZIP打包工具类
- 前端：下载按钮UI
- 依赖：添加ZIP打包依赖