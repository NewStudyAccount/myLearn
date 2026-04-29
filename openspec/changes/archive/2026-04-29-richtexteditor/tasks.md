## 1. 依赖安装

- [x] 1.1 安装 md-editor-v3 依赖（`npm install md-editor-v3`）

## 2. 编辑器组件封装

- [x] 2.1 创建 MarkdownEditor.vue 组件，封装 md-editor-v3 的 MdEditor，支持 v-model 双向绑定，默认 wysiwyg 模式，使用 defineAsyncComponent 动态导入
- [x] 2.2 创建 MarkdownPreview.vue 组件，封装 md-editor-v3 的 MdPreview，用于内容详情渲染，使用 defineAsyncComponent 动态导入

## 3. 页面改造

- [x] 3.1 改造 `views/blog/content/index.vue` 编辑表单，将 content 字段的 `<el-input>` 替换为 MarkdownEditor 组件
- [x] 3.2 改造 `views/blog/content/index.vue` 详情展示，将 content 字段的纯文本显示替换为 MarkdownPreview 组件

## 4. 验证

- [x] 4.1 验证编辑器所见即所得模式与源码模式切换正常
- [x] 4.2 验证编辑器输出为 Markdown 格式字符串，提交后后端正常存储
- [x] 4.3 验证详情页 Markdown 渲染显示正常
