## Context

当前文章管理（SysArticle）和内容管理（SysArticleContent）是两个独立页面，用户需分别操作。Markdown 编辑器（md-editor-v3）已集成但缺少文件上传功能。后端已有 OSS 上传接口 `POST /project/sysOssFile/upload`，前端已有 `uploadSysOssFile` API 和 `ImageUpload` 组件。

## Goals / Non-Goals

**Goals:**
- 文章管理页面新增"编辑内容"按钮，通过 `getSysArticleContentByArticleId` 加载内容并打开编辑弹窗
- Markdown 编辑器支持图片上传，上传后自动插入 Markdown 图片语法
- 移除独立的内容管理页面和路由

**Non-Goals:**
- 不改动后端接口
- 不实现非图片文件的上传（仅支持图片）
- 不合并文章和内容的数据表结构

## Decisions

### 1. 编辑器图片上传使用 md-editor-v3 内置 onUploadImg 回调

**选择**: 使用 md-editor-v3 的 `onUploadImg` 属性

**备选方案**:
- **自定义工具栏按钮**: 需要额外开发，与编辑器集成度差
- **onUploadImg 回调**: md-editor-v3 原生支持，用户点击工具栏图片按钮或粘贴图片时触发，上传完成后将 URL 插入编辑器

**理由**: md-editor-v3 原生提供 `onUploadImg` 回调，与编辑器工具栏无缝集成，用户可通过点击图片按钮或拖拽/粘贴触发上传。

### 2. 图片上传复用现有 OSS 上传接口

**选择**: 调用 `uploadSysOssFile` API 上传到 OSS

**理由**: 项目已有完整的 OSS 上传链路（前端 `uploadSysOssFile` → 后端 `OssController.upload`），无需新建接口。

### 3. 内容编辑弹窗放在文章管理页面

**选择**: 在 `article/index.vue` 中新增内容编辑弹窗

**理由**: 用户从文章列表直接操作，无需跳转。弹窗内使用 MarkdownEditor 组件，通过 `getSysArticleContentByArticleId` 加载内容，保存时调用 `updateSysArticleContent`。

### 4. 移除独立内容管理页面

**选择**: 删除 `views/blog/content/index.vue` 及其路由

**理由**: 内容编辑已合并到文章管理，独立页面不再需要，避免用户混淆。

## Risks / Trade-offs

- **[文章无内容记录时点击编辑内容]** → 后端 `queryByArticleId` 可能返回 null，前端需处理空内容情况，显示空编辑器
- **[移除内容管理页面影响现有用户]** → 这是 **BREAKING** 变更，内容管理菜单和路由将被移除
