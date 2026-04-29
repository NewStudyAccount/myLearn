## Context

当前博客文章内容编辑使用普通 `<el-input>` 单行输入框，无法满足 Markdown 长文本编辑需求。后端已完整支持 Markdown 存储（OSS .md 文件 + Flexmark 解析），但前端缺乏对应的编辑和渲染能力。

文章内容（SysArticleContent）与文章元数据（SysArticle）分表存储，通过 articleId 关联。content 字段在数据库中为 mediumtext 类型，后端会将其以 .md 文件上传至 OSS 并保存 contentUrl。

## Goals / Non-Goals

**Goals:**
- 前端集成 Markdown 富文本编辑器，支持所见即所得编辑和源码模式切换
- 编辑器输出内容为 Markdown 格式，与后端现有存储方案无缝衔接
- 文章内容查看详情时支持 Markdown 渲染预览
- 最小化改动范围，仅涉及前端内容编辑页面

**Non-Goals:**
- 不改动后端接口和存储逻辑
- 不合并文章与内容两张表的设计
- 不实现图片拖拽上传到编辑器（使用现有 OSS 上传流程）
- 不实现文章与内容的关联编辑页面改造

## Decisions

### 1. 选择 md-editor-v3 作为 Markdown 编辑器

**选择**: md-editor-v3

**备选方案**:
- **Vditor**: 功能强大但体积较大，配置复杂
- **md-editor-v3**: 专为 Vue 3 设计，支持所见即所得/分屏/源码三种模式，TypeScript 支持好，轻量
- **TinyMCE**: 传统富文本编辑器，输出 HTML 而非 Markdown，不符合需求
- **WangEditor v5**: 支持 Markdown 模式但生态较小

**理由**: md-editor-v3 原生支持 Vue 3 + TypeScript，提供所见即所得编辑模式，输出 Markdown 格式，与后端存储方案完全匹配。社区活跃，文档完善。

### 2. 编辑器模式默认使用所见即所得（wysiwyg）

**选择**: 默认 wysiwyg 模式，用户可切换至源码模式

**理由**: 非技术用户友好，所见即所得降低 Markdown 语法门槛。保留源码模式供高级用户使用。

### 3. 内容预览使用 md-editor-v3 内置预览组件

**选择**: 使用 MdPreview 组件渲染详情

**理由**: 与编辑器使用同一渲染引擎，样式一致，无需额外引入 Markdown 渲染库。

## Risks / Trade-offs

- **[包体积增加]** → md-editor-v3 约 500KB（gzip 后约 150KB），可通过动态导入优化
- **[编辑器样式与 Element Plus 风格差异]** → 通过自定义主题 CSS 变量适配
- **[md-editor-v3 维护风险]** → 该库社区活跃、持续更新，风险可控
