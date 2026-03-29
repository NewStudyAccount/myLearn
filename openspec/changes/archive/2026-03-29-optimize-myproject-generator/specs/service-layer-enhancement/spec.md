## ADDED Requirements

### Requirement: Service接口包含常用业务方法
生成的Service接口 SHALL 继承IService并声明常用业务方法，使开发者无需手动添加基础CRUD操作。

#### Scenario: 生成Service接口包含分页查询方法
- **WHEN** 调用代码生成器为任意表生成Service层代码
- **THEN** 生成的Service接口包含`getPageList(Page<T> page, Wrapper<T> queryWrapper)`方法声明

#### Scenario: 生成Service接口包含条件查询方法
- **WHEN** 调用代码生成器为任意表生成Service层代码
- **THEN** 生成的Service接口包含`getOne(Wrapper<T> queryWrapper)`和`listByCondition(Wrapper<T> queryWrapper)`方法声明

#### Scenario: 生成Service接口包含批量操作方法
- **WHEN** 调用代码生成器为任意表生成Service层代码
- **THEN** 生成的Service接口包含`saveBatch`和`removeByIds`方法声明（继承自IService）

### Requirement: Service实现类包含方法实现
生成的Service实现类 SHALL 实现Service接口中声明的所有业务方法。

#### Scenario: 分页查询方法实现正确
- **WHEN** 调用代码生成器生成Service实现类
- **THEN** 实现类包含`getPageList`方法，使用MyBatis-Plus的Page对象进行分页查询

#### Scenario: 条件查询方法实现正确
- **WHEN** 调用代码生成器生成Service实现类
- **THEN** 实现类包含`getOne`和`listByCondition`方法实现，使用MyBatis-Plus的QueryWrapper
