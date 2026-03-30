## 1. 分析现有实现

- [x] 1.1 分析 FrontendVueGenerator 现有上下文变量
- [x] 1.2 分析 FrontendApiGenerator 的 API 函数命名规则
- [x] 1.3 确定需要添加的上下文变量（API函数名、类型名、文件路径）

## 2. 修改上下文构建

- [x] 2.1 在 generate() 方法中添加 API 函数名变量（getList${baseName}等）
- [x] 2.2 添加类型名变量（${baseName}）
- [x] 2.3 添加 API 文件路径变量（与 FrontendApiGenerator 保持一致）

## 3. 更新 Vue 模板

- [x] 3.1 修改 import 部分，添加 Vue Composition API import
- [x] 3.2 添加动态 API 函数 import 生成逻辑
- [x] 3.3 添加动态类型 import 生成逻辑
- [x] 3.4 添加 Element Plus 组件 import
- [x] 3.5 确保 import 语句顺序符合规范

## 4. 验证与测试

- [x] 4.1 使用测试数据生成 Vue 组件，验证 import 语句正确性
- [x] 4.2 验证生成的代码语法正确，无编译错误
- [x] 4.3 验证与 FrontendApiGenerator 生成的 API 文件匹配