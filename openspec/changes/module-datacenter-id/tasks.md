## 1. 常量与模块实例定义

- [x] 1.1 在 `SnowflakeIdUtil` 中添加模块 datacenterId 常量：DATACENTER_ID_DEFAULT=0, DATACENTER_ID_SYSTEM=1, DATACENTER_ID_OSS=2, DATACENTER_ID_BLOG=3
- [x] 1.2 为每个模块创建对应的静态实例（SYSTEM_INSTANCE、OSS_INSTANCE、BLOG_INSTANCE）

## 2. 模块级静态方法

- [x] 2.1 添加 `systemNextId()` 静态方法，使用 SYSTEM_INSTANCE 生成ID
- [x] 2.2 添加 `ossNextId()` 静态方法，使用 OSS_INSTANCE 生成ID
- [x] 2.3 添加 `blogNextId()` 静态方法，使用 BLOG_INSTANCE 生成ID
- [x] 2.4 更新 Javadoc 注释，补充模块方法说明和 datacenterId 映射表

## 3. 验证

- [x] 3.1 编写单元测试，验证各模块方法生成的ID包含正确的 datacenterId
- [x] 3.2 编写单元测试，验证模块常量值正确
- [x] 3.3 验证原有 `nextId()` 方法不受影响
