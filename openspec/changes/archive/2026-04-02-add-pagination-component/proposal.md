## Why

在数据列表场景中，分页是必不可少的功能。当前项目虽然引入了 Element Plus，但缺少统一的分页组件封装，导致各页面分页实现不一致、维护成本高，且无法灵活应对不同的分页需求。

## What Changes

- 新增一个可复用的分页组件 `Pagination`
- 支持常见的分页配置：每页条数、总数、当前页码、页码切换事件
- 支持自定义布局（如总数、每页条数选择器、跳转等）
- 与现有 Vue 3 + Element Plus + TypeScript 技术栈无缝集成

## Capabilities

### New Capabilities

- `pagination`: 封装 Element Plus 的分页组件，提供统一的 API 和样式，支持多种布局模式

### Modified Capabilities

（无）

## Impact

- 新增组件文件：`src/components/Pagination/index.vue`
- 可能在多个列表页面中复用该组件
- 依赖 Element Plus 的 ElPagination 组件
- 需要统一类型定义和样式规范
