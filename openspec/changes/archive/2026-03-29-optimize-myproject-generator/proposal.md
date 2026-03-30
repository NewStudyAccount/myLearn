## Why

myproject-generator模块生成的代码存在两个问题：1) Service层只生成空壳代码，缺少基本业务方法实现，开发者仍需手动添加常用CRUD方法；2) 生成的类名首字母小写，违反Java类命名规范，导致代码不规范且可能引起编译错误。

## What Changes

- **Service层增强**: 生成的Service接口和实现类增加常用业务方法，包括：分页查询、条件查询、批量保存、逻辑删除等
- **类名规范化**: 修复类名生成逻辑，确保所有生成的类名（Entity、Service、Controller、Mapper）首字母大写
- **文件命名修复**: 修复ZIP下载时文件命名与类名不一致的问题

## Capabilities

### New Capabilities
- `service-layer-enhancement`: Service层代码生成增强，包含基本业务方法实现
- `class-naming-convention`: 类名生成规范化，遵循Java命名约定

### Modified Capabilities
（无现有规范需要修改）

## Impact

- 影响模块：myproject-generator
- 影响文件：ServiceGenerator.java, EntityGenerator.java, TableReader.java, GeneratorController.java
- 影响生成的代码：所有Service层代码、Entity类名、Controller类名
- 无API变更，对外接口保持兼容
