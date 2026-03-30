## Context

当前前端项目的 `style.css` 文件中，`#app` 选择器设置了 `width: 1126px` 和 `max-width: 100%`，导致页面在大屏幕上居中显示，两侧有大量空白。同时设置了 `border-inline: 1px solid var(--border)`，添加了不必要的边框。

## Goals / Non-Goals

**Goals:**
- 页面铺满整个浏览器窗口
- 移除固定宽度限制
- 保持响应式设计

**Non-Goals:**
- 不改变页面内容布局
- 不修改业务逻辑
- 不添加新的样式

## Decisions

### Decision 1: 移除固定宽度
- **选择**: 将 `width: 1126px` 改为 `width: 100%`
- **理由**: 让页面自适应浏览器宽度
- **替代方案**: 使用 `min-width` - 仍然会限制最小宽度

### Decision 2: 保留 box-sizing
- **选择**: 保留 `box-sizing: border-box`
- **理由**: 确保 padding 和 border 不会增加元素总宽度
- **替代方案**: 移除 box-sizing - 可能导致布局问题

## Risks / Trade-offs

- **风险**: 移除居中样式可能影响某些页面的视觉效果 → **缓解**: 主布局组件会处理内容居中
- **风险**: 移除边框可能影响某些页面的分隔效果 → **缓解**: 主布局组件有自己的边框样式

## Migration Plan

1. 修改 `style.css` 中的 `#app` 样式
2. 确保 `html` 和 `body` 设置为 100% 高度
3. 测试页面布局是否正确
