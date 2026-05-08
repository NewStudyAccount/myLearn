## 1. 路由配置调整

- [x] 1.1 在 `router/index.ts` 中将 `blog/doc/:articleId` 路由从 MainLayout 的 children 数组中移出，作为顶层路由独立注册，保留 `meta: { requiresAuth: true, title: '文档展示' }`

## 2. 文档展示页全屏布局重构

- [x] 2.1 重构 `views/blog/doc/index.vue` 的模板结构，移除对 MainLayout 布局的依赖，实现全屏布局
- [x] 2.2 页面顶部保留返回按钮和文章标题区域
- [x] 2.3 页面主体区域采用左右双栏布局：左侧为文档 HTML 内容区域，右侧为目录侧边栏（宽度 240px）
- [x] 2.4 文档内容区域设置最大宽度（如 800px）并居中，确保阅读舒适度
- [x] 2.5 目录侧边栏使用 `position: sticky` 实现滚动固定

## 3. 目录生成与渲染

- [x] 3.1 定义 `TocItem` TypeScript 接口，包含 `id`（string）、`text`（string）、`level`（number）字段
- [x] 3.2 在 HTML 内容渲染后（`onMounted` + `nextTick`），遍历 `.markdown-body` 下的 h1-h6 元素，提取标题文本和层级
- [x] 3.3 为每个标题元素动态设置 id 属性（格式 `heading-{index}`）
- [x] 3.4 生成 `TocItem[]` 目录数据并渲染为目录列表，不同层级使用缩进区分

## 4. 目录点击跳转

- [x] 4.1 为目录项绑定点击事件，点击时调用 `document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })` 平滑滚动到对应标题

## 5. 目录滚动高亮

- [x] 5.1 使用 `IntersectionObserver` 监听所有标题元素的可见性变化
- [x] 5.2 当标题进入视口时，将该标题对应的目录项标记为活跃（`activeTocId`）
- [x] 5.3 活跃目录项应用高亮样式（如左侧边框 + 文字颜色变化）
- [x] 5.4 在组件卸载时（`onUnmounted`）断开 IntersectionObserver

## 6. 样式优化

- [x] 6.1 编写全屏文档页的整体样式，确保页面背景、间距、字体等与文档阅读场景匹配
- [x] 6.2 编写目录侧边栏样式：目录项的默认样式、hover 样式、active 高亮样式
- [x] 6.3 确保文档内容区域的 markdown-body 样式在独立页面中正常工作