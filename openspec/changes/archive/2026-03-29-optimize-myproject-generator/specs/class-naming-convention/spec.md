## ADDED Requirements

### Requirement: 生成的类名首字母大写
所有通过代码生成器生成的类名 SHALL 遵循Java UpperCamelCase命名规范，首字母必须大写。

#### Scenario: Entity类名首字母大写
- **WHEN** 为表"user"生成Entity类
- **THEN** 生成的类名为"User"而非"user"

#### Scenario: Service接口名首字母大写
- **WHEN** 为表"sys_role"生成Service接口
- **THEN** 生成的接口名为"SysRoleService"而非"sysRoleService"

#### Scenario: Service实现类名首字母大写
- **WHEN** 为表"order_detail"生成Service实现类
- **THEN** 生成的类名为"OrderDetailServiceImpl"而非"orderDetailServiceImpl"

#### Scenario: Controller类名首字母大写
- **WHEN** 为表"product"生成Controller
- **THEN** 生成的类名为"ProductController"而非"productController"

#### Scenario: Mapper接口名首字母大写
- **WHEN** 为表"category"生成Mapper接口
- **THEN** 生成的接口名为"CategoryMapper"而非"categoryMapper"

### Requirement: ZIP下载文件名与类名一致
代码生成器下载的ZIP文件中，文件名 SHALL 与生成的类名保持一致。

#### Scenario: Service接口文件名正确
- **WHEN** 下载包含Service接口的ZIP文件
- **THEN** 文件名为"ISysRoleService.java"而非"IsysRoleService.java"

#### Scenario: Controller文件名正确
- **WHEN** 下载包含Controller的ZIP文件
- **THEN** 文件名为"ProductController.java"而非"productController.java"
