## Context

当前项目使用 Vue 3 + Element Plus + TypeScript 技术栈。Element Plus 提供了功能丰富的 `ElPagination` 组件，但各业务页面直接使用该组件导致 API 风格不统一、样式难维护。需要封装一个通用分页组件，统一使用方式并支持扩展。

## Goals / Non-Goals

**Goals:**
- 提供统一的分页组件 API，降低业务页面使用成本
- 支持常见分页场景（总数、每页条数、跳转、布局自定义）
- 与 Element Plus 主题风格保持一致
- 使用 TypeScript 类型定义，确保类型安全

**Non-Goals:**
- 不实现虚拟滚动或大数据量优化（超出分页组件职责）
- 不替换 Element Plus 分页组件，仅做封装
- 不处理数据请求逻辑（仅负责 UI 与事件）

## Decisions

1. **封装而非重写**  
   直接封装 `ElPagination`，利用其成熟功能，减少开发量和潜在 bug。

2. **Props 设计**  
   采用 `v-model` 双向绑定 `currentPage` 和 `pageSize`，简化页面代码。暴露 `total`、`layout`、`pageSizes` 等常用属性，保持与 Element Plus 一致。

3. **事件透传**  
   将 `size-change`、`current-change`、`prev-click`、`next-click` 等事件透传给父组件，保持灵活性。

4. **目录结构**  
   组件放置在 `src/components/Pagination/`，包含 `index.vue`（主组件）和可选的类型定义文件。

## Risks / Trade-offs

- **风险**：Element Plus 版本升级可能导致 API 变动 → 跟随官方版本更新，定期同步。
- **权衡**：封装层级增加可能导致部分高级用法受限 → 暴露 `elPaginationProps` 支持透传，确保灵活性。
