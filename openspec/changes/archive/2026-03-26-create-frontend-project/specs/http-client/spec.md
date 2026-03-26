## ADDED Requirements

### Requirement: Axios 安装
系统 SHALL 安装 Axios HTTP 客户端。

#### Scenario: Axios 安装成功
- **WHEN** 执行 `npm install axios` 命令
- **THEN** package.json 中添加 axios 依赖
- **AND** 可以使用 Axios 发送 HTTP 请求

### Requirement: Axios 实例封装
系统 SHALL 创建统一的 Axios 实例。

#### Scenario: Axios 封装完成
- **WHEN** 项目创建完成
- **THEN** 存在 src/utils/http.ts 或 src/api/http.ts 文件
- **AND** 配置了基础 URL、超时时间等

### Requirement: 请求拦截器配置
系统 SHALL 配置 Axios 请求和响应拦截器。

#### Scenario: 拦截器配置完成
- **WHEN** 发送 HTTP 请求
- **THEN** 请求拦截器自动添加 token 等信息
- **AND** 响应拦截器统一处理错误
