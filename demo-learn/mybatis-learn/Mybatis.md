首先回顾一下在Java Web中，如何连接数据库
创建数据库连接，创建Statement对象，执行SQL语句，处理结果，关闭数据库连接。

原生jdbc的方式

JDBC通过PreparedStatement接口提供了设置参数的方法，主要原理如下：
位置参数索引：参数通过从1开始的整数索引标识，而不是名称
类型映射：根据不同数据类型提供相应的set方法
预编译SQL：参数值在SQL执行前绑定到预编译语句中，所以编写调用执行语句时的参数需要按照顺序传入，

```mysql
select * from user where id = ? and name = ? and age = ?;
```

```
见方法 executeQuery
```

为例直观体现名称可以采用Map<String,String>去传查询参数。查询语句中进行处理
```
见方法 executeQuery2
```

返回值处理
```
见方法handleObject，需要传入具体的对象，最后通过反射将查询的结果重新赋值到返回值中
```

完整实现示例见类
```java
TestMain
```

实现示例中的处理返回值时，使用了反射，将查询结果重新赋值给返回值对象。这一部分代码可以后续增加解释描述。






MyBatis 是一个优秀的持久层框架，它封装了 JDBC 的复杂操作，提供了简洁的 API。下面我们从源码层面详细分析 MyBatis 的核心实现原理。
1. 整体架构
   MyBatis 的核心组件包括：
   SqlSessionFactory: 构建 SqlSession 的工厂类
   SqlSession: 执行 SQL 语句的主要接口
   Executor: SQL 执行器，负责查询、更新等操作
   StatementHandler: 语句处理器，封装了 JDBC Statement 的操作
   ParameterHandler: 参数处理器，负责设置 SQL 参数
   ResultSetHandler: 结果集处理器，负责将结果集转换为 Java 对象
   TypeHandler: 类型处理器，处理 Java 类型和 JDBC 类型之间的转换
2. SqlSessionFactory
   工厂模式创建对象
   工厂模式：
   
3. 



### 泛型
泛型的写法
```java
访问修饰符 <T> 返回类型 方法名(参数列表) {
   // 方法体
}
// 返回任意类型的单个对象
public <T> T getObject(Class<T> clazz) {
    try {
        return clazz.newInstance();
    } catch (Exception e) {
        return null;
    }
}
```