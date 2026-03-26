## ADDED Requirements

### Requirement: Pinia 安装
系统 SHALL 安装并配置 Pinia 状态管理。

#### Scenario: Pinia 安装成功
- **WHEN** 执行 `npm install pinia` 命令
- **THEN** package.json 中添加 pinia 依赖
- **AND** 可以创建和使用 Pinia store

### Requirement: Pinia 集成配置
系统 SHALL 在 main.ts 中配置 Pinia。

#### Scenario: Pinia 集成完成
- **WHEN** 项目启动
- **THEN** main.ts 中创建并使用 Pinia 实例
- **AND** 所有组件都可以访问 Pinia store

### Requirement: Pinia 持久化配置
系统 SHALL 安装并配置 pinia-plugin-persistedstate。

#### Scenario: 持久化配置完成
- **WHEN** 安装 pinia-plugin-persistedstate
- **THEN** Pinia 支持状态持久化到 localStorage
- **AND** store 可以配置 persist 选项
