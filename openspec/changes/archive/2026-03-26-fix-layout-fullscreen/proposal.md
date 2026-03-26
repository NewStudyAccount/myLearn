## Why

当前前端项目的布局只占据了浏览器页面的一小部分，没有铺满整个屏幕。原因是 `style.css` 中的 `#app` 选择器设置了固定宽度 `width: 1126px`，导致页面内容居中显示，两侧有大量空白。

## What Changes

- 移除 `style.css` 中 `#app` 的固定宽度限制
- 修改 `#app` 样式，使其宽度为 100%，铺满整个浏览器窗口
- 确保 `html` 和 `body` 元素也设置为 100% 高度
- 清理不需要的居中样式和边框样式

## Capabilities

### New Capabilities
- `fullscreen-layout`: 全屏布局，页面铺满整个浏览器窗口

### Modified Capabilities
（无现有 spec 需要修改）

## Impact

- 修改文件：
  - `src/style.css` - 移除固定宽度，修改布局样式
- 不影响业务逻辑和功能
- 不引入新依赖
