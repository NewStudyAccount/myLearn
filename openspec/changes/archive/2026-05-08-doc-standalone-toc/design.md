## Context

项目基于 Vue 3 + Element Plus + TypeScript 技术栈。当前文档展示页路由配置在 `router/index.ts` 中，作为 MainLayout（包含 SideMenu 侧边栏和 HeaderNav 顶栏）的子路由渲染。MainLayout 使用 Element Plus 的 `el-container` + `el-aside` + `el-header` + `el-main` 布局结构，文档页内容被限制在 `el-main` 区域内，有效宽度约为视口的 70%-80%。

文档展示页当前通过 `v-html` 渲染后端 Flexmark 转换后的 HTML 内容，HTML 中的标题标签（h1-h6）不包含 id 属性，无法直接用于锚点跳转。

## Goals / Non-Goals

**Goals:**
- 文档展示页以全屏独立页面形式展示，不嵌套在管理系统布局中
- 右侧显示文档目录导航，从 HTML 内容自动提取 h1-h6 标题生成
- 点击目录项平滑滚动到对应标题位置
- 滚动时自动高亮当前阅读章节对应的目录项
- 保留返回文章列表的导航功能

**Non-Goals:**
- 不修改后端接口或返回数据格式
- 不实现目录的折叠/展开功能（目录项较少时无需折叠）
- 不实现目录的持久化或用户自定义配置
- 不实现移动端适配的目录抽屉模式（当前仅桌面端）

## Decisions

### 1. 路由配置：将文档路由移至顶层

**选择**：将 `blog/doc/:articleId` 从 MainLayout 的 children 中移出，作为顶层路由独立注册

**理由**：文档展示页需要全屏展示，脱离 MainLayout 的侧边栏和顶栏约束。作为顶层路由，页面拥有完整的视口空间，可自由控制布局。同时保留 `meta: { requiresAuth: true }` 确保路由守卫仍然生效。

**备选方案**：
- 在 MainLayout 中增加全屏模式切换：侵入性强，需修改 MainLayout 逻辑，增加状态管理复杂度
- 使用新窗口打开（window.open）：用户体验差，失去单页应用的路由优势

### 2. 目录生成：前端解析 HTML 提取标题

**选择**：在 HTML 内容渲染后，通过 DOM 操作提取 h1-h6 标题，为每个标题动态添加 id 属性，生成目录数据

**理由**：后端 Flexmark 生成的 HTML 标题不包含 id 属性，需前端补充。在 `onMounted` + `nextTick` 后遍历 `.markdown-body` 下的标题元素，提取文本内容和层级，同时为标题元素设置 id（格式 `heading-{index}`）以支持锚点跳转。纯前端实现，无需修改后端。

**备选方案**：
- 后端在转换时为标题添加 id：需修改 MarkDownUtil 或 Flexmark 配置，侵入后端逻辑
- 使用正则解析 HTML 字符串：不如 DOM 操作可靠，可能误匹配代码块中的标题语法

### 3. 目录布局：右侧固定侧边栏

**选择**：采用 CSS `position: sticky` 实现右侧目录固定，左侧为文档内容区域

**理由**：`sticky` 定位在滚动时自动固定目录，无需 JavaScript 监听滚动计算位置，性能好且实现简洁。目录宽度固定 240px，文档内容区域自适应剩余宽度。

**布局结构**：
