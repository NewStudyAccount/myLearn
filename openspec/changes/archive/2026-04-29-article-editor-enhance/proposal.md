## Why

当前文章管理（元数据）和内容管理（正文）是两个独立页面，用户需要分别操作，体验割裂。同时 Markdown 编辑器不支持图片/文件上传，用户无法在编辑内容时插入图片。需要将内容编辑入口合并到文章管理页面，并为编辑器增加文件上传能力。

## What Changes

- 文章管理页面操作列新增"编辑内容"按钮，点击后通过 `getSysArticleContentByArticleId` 接口获取内容，打开 Markdown 编辑器弹窗
- Markdown 编辑器增加图片/文件上传支持，上传后自动将文件 URL 插入到 Markdown 内容中
- 移除独立的内容管理页面（`views/blog/content/index.vue`），内容编辑统一从文章管理入口进入

## Capabilities

### New Capabilities
- `editor-file-upload`: Markdown 编辑器文件上传能力，支持在编辑器中上传图片/文件并自动插入 URL

### Modified Capabilities
- `markdown-editor`: 编辑器需支持从文章管理页面打开、传入 articleId 加载内容，并集成文件上传回调
