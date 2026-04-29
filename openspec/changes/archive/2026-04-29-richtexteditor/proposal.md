## Why

文章内容编辑当前使用普通 `<el-input>` 单行输入框，完全无法编辑 Markdown 格式的长文本内容。而后端已完整支持 Markdown 存储（OSS .md 文件）和解析（Flexmark），前端亟需引入 Markdown 富文本编辑器来匹配后端能力，提供所见即所得的编辑体验。

## What Changes

- 前端引入 Markdown 富文本编辑器组件（支持所见即所得编辑 + Markdown 源码模式切换）
- 替换文章内容页面的 `<el-input>` 为富文本编辑器组件
- 新增 Markdown 内容渲染预览能力（用于文章内容查看详情）
- 文章内容以 Markdown 格式存储，与后端现有 OSS .md 存储方案保持一致

## Capabilities

### New Capabilities
- `markdown-editor`: Markdown 富文本编辑器集成，提供所见即所得编辑、源码模式切换、内容以 Markdown 格式输出

### Modified Capabilities

（无现有规格需要修改）

## Impact

- **前端依赖**：新增 Markdown 编辑器 npm 包（如 md-editor-v3）
- **前端页面**：`myproject-frontend/src/views/blog/content/index.vue` 需改造表单和详情展示
- **前端 API**：`sysArticleContentApi.ts` 接口不变，content 字段仍为 Markdown 字符串
- **后端**：无需改动，现有 Flexmark + OSS 方案已支持 Markdown 存储和解析
