## ADDED Requirements

### Requirement: 文档展示页 SHALL 在右侧显示目录导航

系统 SHALL 在文档展示页右侧显示目录（TOC）侧边栏，目录内容 SHALL 从渲染后的 HTML 内容中自动提取 h1-h6 标题生成。目录 SHALL 显示标题文本和层级缩进。

#### Scenario: 文档包含多级标题时生成目录
- **WHEN** 文档 HTML 内容包含 h1、h2、h3 等多级标题
- **THEN** 右侧目录显示所有标题项，按文档顺序排列
- **AND** 不同层级的标题在目录中有对应的缩进

#### Scenario: 文档无标题时目录为空
- **WHEN** 文档 HTML 内容不包含任何 h1-h6 标题
- **THEN** 右侧目录区域为空或显示"无目录"提示

#### Scenario: 文档内容加载完成后生成目录
- **WHEN** 后端接口成功返回 HTML 内容并渲染完成
- **THEN** 系统自动提取标题并生成目录

### Requirement: 目录项 SHALL 支持点击跳转

系统 SHALL 为每个目录项绑定点击事件，点击后页面 SHALL 平滑滚动到对应标题位置。

#### Scenario: 点击目录项跳转到标题
- **WHEN** 用户点击目录中的某个标题项
- **THEN** 页面平滑滚动到该标题在文档中的位置
- **AND** 该标题在视口中可见

### Requirement: 目录 SHALL 根据滚动位置高亮当前章节

系统 SHALL 根据用户当前阅读位置，自动高亮目录中对应的标题项。当用户滚动文档时，目录中当前可见章节对应的项 SHALL 以视觉样式区分。

#### Scenario: 滚动到新章节时高亮切换
- **WHEN** 用户滚动文档，新标题进入视口可见区域
- **THEN** 目录中该标题对应项高亮显示
- **AND** 之前高亮的目录项取消高亮

#### Scenario: 页面初始加载时高亮第一个标题
- **WHEN** 文档内容加载完成，第一个标题在视口中可见
- **THEN** 目录中第一个标题项高亮显示

### Requirement: 系统 SHALL 为 HTML 标题动态添加 id 属性

系统 SHALL 在 HTML 内容渲染后，为文档中所有 h1-h6 标题动态添加 id 属性（格式 `heading-{index}`），以支持锚点定位和目录跳转。

#### Scenario: 标题元素获得 id 属性
- **WHEN** 文档 HTML 内容渲染完成
- **THEN** 所有 h1-h6 标题元素具有唯一的 id 属性
- **AND** id 属性值为 `heading-0`、`heading-1`、`heading-2` 等递增格式
