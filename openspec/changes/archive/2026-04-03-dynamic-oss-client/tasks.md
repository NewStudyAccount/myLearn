## 1. 数据库设计与初始化

- [x] 1.1 创建`sys_oss_config`表，包含字段：id、config_name、provider、endpoint、access_key、secret_key、bucket_name、region、extra_config、is_active、created_at、updated_at
- [x] 1.2 编写数据库初始化SQL脚本，包含表结构和示例数据
- [x] 1.3 在项目中添加MyBatis-Plus依赖配置

## 2. MyBatis-Plus数据访问层

- [x] 2.1 创建`OssConfig`实体类，使用MyBatis-Plus注解映射表字段
- [x] 2.2 创建`OssConfigMapper`接口，继承BaseMapper实现基础CRUD
- [x] 2.3 创建`OssConfigService`接口和`OssConfigServiceImpl`实现类
- [x] 2.4 实现配置查询方法：按config_name查询、查询所有激活配置
- [x] 2.5 实现配置管理方法：创建、更新、删除配置（包含参数验证）

## 3. 客户端工厂模式实现

- [x] 3.1 定义`OssClientFactory`接口，包含`createClient(OssConfig config)`方法
- [x] 3.2 实现`AliyunOssClientFactory`，创建阿里云OSS客户端
- [x] 3.3 实现`MinioClientFactory`，创建MinIO客户端
- [x] 3.4 创建`OssClientFactoryProvider`，根据provider类型返回对应工厂
- [x] 3.5 实现`OssClientService`，整合配置查询和客户端创建逻辑

## 4. Redis缓存集成

- [x] 4.1 添加Redis依赖和配置（Spring Boot Starter Data Redis）
- [x] 4.2 创建`OssConfigCacheService`，使用RedisTemplate实现配置缓存
- [x] 4.3 实现缓存策略：查询时先查缓存，缓存没有则查数据库并写入缓存
- [x] 4.4 实现缓存失效：配置更新或删除时清除对应缓存
- [x] 4.5 配置缓存TTL（默认5分钟）和序列化方式

## 5. 配置刷新机制

- [x] 5.1 创建`CacheRefreshService`，提供手动刷新所有缓存的方法
- [x] 5.2 实现REST接口`/oss/config/refresh`用于手动触发缓存刷新
- [x] 5.3 可选：使用`@Scheduled`实现定时缓存刷新（可配置间隔）

## 6. 向后兼容性处理

- [x] 6.1 分析现有OSS客户端使用点，确定兼容性需求
- [x] 6.2 创建`OssClientFacade`门面类，封装新旧两种获取客户端的方式
- [x] 6.3 实现降级策略：当数据库或Redis不可用时，回退到静态配置
- [x] 6.4 更新现有代码，逐步迁移到使用新的`OssClientService`

## 7. 错误处理与验证

- [x] 7.1 实现配置参数验证：必填字段检查、provider类型验证、连接参数格式验证
- [x] 7.2 创建自定义异常类：`OssConfigNotFoundException`、`OssClientCreateException`
- [x] 7.3 添加详细日志记录：配置加载、客户端创建、缓存操作等关键节点
- [x] 7.4 实现连接测试功能：创建客户端后验证连接是否成功

## 8. 测试

- [x] 8.1 编写`OssConfigService`单元测试，覆盖CRUD和验证逻辑
- [x] 8.2 编写客户端工厂单元测试，验证各提供商客户端创建
- [x] 8.3 编写缓存服务集成测试，验证Redis读写和失效逻辑
- [x] 8.4 编写端到端测试：从配置查询到客户端创建的完整流程
- [x] 8.5 编写异常场景测试：数据库连接失败、Redis不可用等降级情况

## 9. 文档与部署

- [x] 9.1 更新项目README，说明新的动态配置功能和使用方式
- [x] 9.2 创建数据库迁移指南，说明如何创建sys_oss_config表
- [x] 9.3 编写配置管理API文档（如果暴露REST接口）
- [x] 9.4 制定部署检查清单：数据库变更、Redis配置、功能验证步骤