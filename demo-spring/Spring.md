非常好！我们从 **第一阶段：Spring 核心原理（IoC + AOP + 事务）** 开始，**以原理为纲、源码为证、实践为辅**，一步步深入。

我们将聚焦 **IoC 容器**，因为它是 Spring 的基石。今天先搞定 **IoC 的核心：Bean 是如何被 Spring 容器管理的？**



## 🎯 学习目标（本节结束时你能回答）

1. Spring 容器启动时做了什么？
2. `BeanDefinition` 是什么？它存了哪些信息？
3. Bean 的完整生命周期是怎样的？
4. `@Autowired` 是在什么时候、由谁处理的？
5. 单例 Bean 最终存在哪里？如何取出？

我们**边讲原理，边看关键源码**（基于 Spring Framework 5.3.x）。



## 第一步：Spring 容器启动入口

### 📌 核心方法：`AbstractApplicationContext.refresh()`

> 这是 Spring IoC 容器的“总开关”，所有初始化逻辑从此开始。

```java
// org.springframework.context.support.AbstractApplicationContext
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        // 1. 准备刷新（记录启动时间、设置活跃状态等）
        prepareRefresh();

        // 2. 告诉子类刷新内部 BeanFactory（通常是 DefaultListableBeanFactory）
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 3. 配置 BeanFactory（添加一些标准后置处理器、注册环境 Bean 等）
        prepareBeanFactory(beanFactory);

        // 4. 允许子类进一步处理 BeanFactory（如注册 BeanPostProcessor）
        postProcessBeanFactory(beanFactory);

        // 5. ★★★ 调用所有 BeanFactoryPostProcessor（如 ConfigurationClassPostProcessor）
        invokeBeanFactoryPostProcessors(beanFactory);

        // 6. ★★★ 注册所有 BeanPostProcessor（如 AutowiredAnnotationBeanPostProcessor）
        registerBeanPostProcessors(beanFactory);

        // 7. 初始化消息源、事件广播器等
        initMessageSource();
        initApplicationEventMulticaster();

        // 8. 模板方法，留给子类（如 Spring Boot 嵌入 Web 容器）
        onRefresh();

        // 9. 注册监听器
        registerListeners();

        // 10. ★★★ 实例化所有非懒加载的单例 Bean！
        finishBeanFactoryInitialization(beanFactory);

        // 11. 完成刷新（发布事件、生命周期回调）
        finishRefresh();
    }
}
```

> 🔥 **重点三步**：
>
> - `invokeBeanFactoryPostProcessors` → **处理 `@Configuration`、`@ComponentScan`、注册 `BeanDefinition`**
> - `registerBeanPostProcessors` → **注册 `@Autowired` 处理器等**
> - `finishBeanFactoryInitialization` → **创建所有单例 Bean**





##  3. 配置 BeanFactory（添加一些标准后置处理器、注册环境 Bean 等）



```java
// 3. 配置 BeanFactory（添加一些标准后置处理器、注册环境 Bean 等）
prepareBeanFactory(beanFactory);

protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// Tell the internal bean factory to use the context's class loader etc.
		beanFactory.setBeanClassLoader(getClassLoader());
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

		// Configure the bean factory with context callbacks.
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
		beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationStartupAware.class);

		// BeanFactory interface not registered as resolvable type in a plain factory.
		// MessageSource registered (and found for autowiring) as a bean.
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);

		// Register early post-processor for detecting inner beans as ApplicationListeners.
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

		// Detect a LoadTimeWeaver and prepare for weaving, if found.
		if (!NativeDetector.inNativeImage() && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			// Set a temporary ClassLoader for type matching.
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}

		// Register default environment beans.
		if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
			beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
		}
		if (!beanFactory.containsLocalBean(APPLICATION_STARTUP_BEAN_NAME)) {
			beanFactory.registerSingleton(APPLICATION_STARTUP_BEAN_NAME, getApplicationStartup());
		}
	}
```



##### 1. **设置类加载器和表达式解析器**

```java
beanFactory.setBeanClassLoader(getClassLoader());
beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
```

- **`setBeanClassLoader`**：设置 Bean 的类加载器为当前上下文的类加载器，确保在需要动态加载类时使用正确的 ClassLoader。
- **`setBeanExpressionResolver`**：配置 Spring 表达式语言（SpEL）的解析器，允许在 Bean 配置中使用如 `#{systemProperties.myProp}` 的表达式。
- **`addPropertyEditorRegistrar`**：注册属性编辑器（PropertyEditor），用于将字符串（如配置文件中的值）转换为特定类型（如 `Resource`、`File` 等）。`ResourceEditorRegistrar` 主要支持 `Resource` 类型的自动转换。

##### 2. **注册 `ApplicationContextAwareProcessor` 并忽略某些 Aware 接口的自动注入**

```java
beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
// ... 其他 ignoreDependencyInterface 调用
```

- **`ApplicationContextAwareProcessor`** 是一个 `BeanPostProcessor`，它会在 Bean 初始化后检查是否实现了某些 `*Aware` 接口（如 `ApplicationContextAware`、`EnvironmentAware` 等），如果实现了就自动注入对应的依赖（如 `ApplicationContext`、`Environment` 等）。
- **`ignoreDependencyInterface`** 告诉 Spring：这些接口的 setter 方法（如 `setApplicationContext`）**不要通过自动装配（byType）来注入**，因为它们会由 `ApplicationContextAwareProcessor` 来处理。避免重复或冲突注入。

##### 3. **注册可解析的依赖类型（Resolvable Dependencies）**

```java
beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
beanFactory.registerResolvableDependency(ResourceLoader.class, this);
// ... 其他 registerResolvableDependency 调用
```

- 这些调用使得在 Bean 中可以直接通过 **构造函数或 setter 注入** 这些类型，而无需显式定义 Bean。
- 例如，如果某个 Bean 的构造函数参数是 `ApplicationContext`，Spring 会自动传入当前的 `ApplicationContext` 实例。
- 注册的类型包括：
  - `BeanFactory`
  - `ResourceLoader`（ApplicationContext 本身实现了该接口）
  - `ApplicationEventPublisher`
  - `ApplicationContext`

##### 4. **注册 `ApplicationListenerDetector`**

```java
beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
```

- 这个 `BeanPostProcessor` 用于检测那些实现了 `ApplicationListener` 接口的 Bean，并将它们注册到 Spring 的事件广播器中，使其能接收 `ApplicationEvent`。
- 如果 Bean 是内部 Bean（inner bean）或作用域不符合要求，会将其从监听器列表中移除。

##### 5. **支持 LoadTimeWeaving（如果启用）**

```java
if (!NativeDetector.inNativeImage() && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
    beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
    beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
}
```

- 如果容器中存在名为 `loadTimeWeaver` 的 Bean（通常由 `<context:load-time-weaver/>` 配置），说明启用了 **AspectJ 的加载时织入（Load-Time Weaving）**。
- 此时注册 `LoadTimeWeaverAwareProcessor`，用于将 `LoadTimeWeaver` 注入到实现了 `LoadTimeWeaverAware` 的 Bean 中。
- 设置临时 ClassLoader 用于类型匹配（因为织入可能改变类结构）。

##### 6. **注册环境相关的单例 Bean**

```java
if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
    beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
}
// ... 类似地注册 systemProperties、systemEnvironment、applicationStartup
```

- 将 `Environment`、系统属性（`Map<String, Object>`）、系统环境变量（`Map<String, String>`）、`ApplicationStartup` 注册为**单例 Bean**，使得应用中的其他 Bean 可以通过 `@Autowired` 或 `@Value` 等方式引用它们。
- 例如：`@Autowired private Environment env;` 就会注入这里注册的 `Environment` 实例。

### 总结

`prepareBeanFactory` 方法的核心目标是：

> **将 ApplicationContext 的高级功能“桥接”到底层的 BeanFactory，使得普通 Bean 也能享受 Spring 容器提供的各种服务（如环境感知、事件发布、表达式解析、资源加载等）。**

这是 Spring 容器启动过程中**连接“基础容器”（BeanFactory）和“高级容器”（ApplicationContext）** 的关键一步。



## 4、允许子类进一步处理 BeanFactory（如注册 BeanPostProcessor）

postProcessBeanFactory(beanFactory);

```
是 Spring 容器初始化过程中一个典型的模板方法（Template Method）设计模式的体现，用于允许子类在 BeanFactory 完成基础配置后，执行自定义的扩展逻辑。
```

它的作用：**给子类一个干预 BeanFactory 的“钩子”（hook）**

`postProcessBeanFactory` 方法在 `AbstractApplicationContext` 中是**空实现**：

```java
protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    // 默认什么都不做，留给子类覆盖
}
```

但 **Spring 的具体 ApplicationContext 子类会重写它**，用于注入特定功能。典型例子：

#### ✅ `GenericWebApplicationContext`

- 注册 `ServletContextAwareProcessor`
- 注册 `ServletContext`、`ServletConfig` 为可解析依赖

#### ✅ `AnnotationConfigServletWebServerApplicationContext`（Spring Boot Web 环境）

- 注册 `WebApplicationContextServletContextAwareProcessor`
- 可能注册 Web 相关的 BeanPostProcessor

#### ✅ `AbstractRefreshableWebApplicationContext`

```java
@Override
protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
    beanFactory.ignoreDependencyInterface(ServletContextAware.class);
    beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
    // 注册 Web 相关的单例
    WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
    WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
}
```





## 5、★★★ 调用所有 BeanFactoryPostProcessor（如 ConfigurationClassPostProcessor）

```
invokeBeanFactoryPostProcessors(beanFactory);
```

**处理 `@Configuration`、`@ComponentScan`、注册 `BeanDefinition`**





```
preInstantiateSingletons
```





// 4. 允许子类进一步处理 BeanFactory（如注册 BeanPostProcessor）
        postProcessBeanFactory(beanFactory);









## 第二步：Bean 注册 —— `BeanDefinition` 是什么？







### 一、整体阶段概览（以 `AnnotationConfigApplicationContext` 为例）

```
1. 容器创建（new ApplicationContext）
   └─ 加载配置类（@Configuration）
2. refresh()
   ├─ ① prepareRefresh()                // 准备环境
   ├─ ② obtainFreshBeanFactory()        // 创建/刷新 BeanFactory
   ├─ ③ prepareBeanFactory()            // 配置 BeanFactory（注册 Aware 接口等）
   ├─ ④ postProcessBeanFactory()        // 子类可扩展（如 WebApplicationContext）
   ├─ ⑤ invokeBeanFactoryPostProcessors()  // ⭐【关键】加载 BeanDefinition
   ├─ ⑥ registerBeanPostProcessors()     // ⭐【关键】注册 BeanPostProcessor 实例
   ├─ ⑦ initMessageSource()             // 国际化
   ├─ ⑧ initApplicationEventMulticaster() // 事件广播器
   ├─ ⑨ onRefresh()                     // Web 容器扩展点
   ├─ ⑩ registerListeners()             // 注册事件监听器
   ├─ ⑪ finishBeanFactoryInitialization() // ⭐【关键】实例化所有非懒加载单例 Bean
   └─ ⑫ finishRefresh()                 // 发布 ContextRefreshedEvent
```



### 二、各类子类/实现类的加载时机详解

#### 1. **配置类（`@Configuration` 类）**

- **何时加载**：在 `refresh()` 第 ⑤ 步 `invokeBeanFactoryPostProcessors()` 中
- **关键处理器**：`ConfigurationClassPostProcessor`
- **过程**：
  - 扫描 `@ComponentScan` 包
  - 解析 `@Bean` 方法 → 生成 `BeanDefinition`
  - **此时只是注册 BeanDefinition，不实例化！**
- ✅ **子类（如 `MyConfig extends BaseConfig`）**：只要被扫描到，就会被解析

#### 2. **普通 Bean（`@Component`, `@Service`, `@Repository` 等）**

- **BeanDefinition 注册**：同上，在第 ⑤ 步完成（通过 `ClassPathBeanDefinitionScanner`）
- **实例化时机**：第 ⑪ 步 `finishBeanFactoryInitialization()`
  - 遍历所有非懒加载（`lazy-init=false`）的单例 Bean
  - 调用 `getBean()` → 触发 `createBean()` → 实例化 + 初始化
- ✅ **子类会被正常实例化**：Spring 通过 `BeanDefinition.getBeanClassName()` 获取具体类名

#### 3. **`BeanFactoryPostProcessor` 的实现类**

- **作用**：在 Bean 实例化前修改 `BeanDefinition`（如 `@Value` 替换）
- **加载时机**：
  - 如果是 **普通 Bean**（如 `@Component`），先被注册为普通 Bean
  - 在第 ⑤ 步 `invokeBeanFactoryPostProcessors()` 中**优先实例化并执行**
- ✅ **典型子类**：
  - `ConfigurationClassPostProcessor`（处理 `@Configuration`）
  - `PropertySourcesPlaceholderConfigurer`（处理 `${}`）

> ⚠️ 注意：这些类**必须在 Bean 实例化前就准备好**，所以 Spring 会 **提前实例化** 它们。

#### 4. **`BeanPostProcessor` 的实现类**

- **作用**：在 Bean 初始化前后做增强（如 AOP 代理、`@Autowired` 注入）
- **加载时机**：
  - BeanDefinition 在第 ⑤ 步注册
  - **实例化在第 ⑥ 步 `registerBeanPostProcessors()`**
  - **早于普通 Bean 的实例化（第 ⑪ 步）**
- ✅ **典型子类**：
  - `AutowiredAnnotationBeanPostProcessor`（处理 `@Autowired`）
  - `CommonAnnotationBeanPostProcessor`（处理 `@PostConstruct`）
  - `AnnotationAwareAspectJAutoProxyCreator`（AOP 代理）

> 💡 这就是为什么 `@Autowired` 能在 `@Bean` 方法中使用 —— BPP 已提前加载。

#### 5. **AOP 代理类（JDK Proxy / CGLib 子类）**

- **不是你写的类，而是 Spring 动态生成的子类/代理类**
- **生成时机**：在目标 Bean **初始化完成前**（`initializeBean()` 阶段）
  - 由 `AbstractAutoProxyCreator.postProcessAfterInitialization()` 触发
  - 如果匹配切点，则用 CGLib 生成子类（或 JDK Proxy）
- ✅ **例如**：
  - 你的 `OrderService` 被 `@Transactional` 标注
  - Spring 会生成 `OrderService$$EnhancerBySpringCGLIB$$xxx` 作为代理

#### 6. **懒加载 Bean（`@Lazy`）**

- **BeanDefinition 注册**：第 ⑤ 步
- **实例化时机**：**第一次调用 `getBean()` 时**（可能是运行时任意时刻）
- ✅ 子类同样适用

### 三、一张表总结“各类子类加载时机”

| 类型                       | 代表注解/接口               | BeanDefinition 注册时机 | 实例化时机              | 用途             |
| -------------------------- | --------------------------- | ----------------------- | ----------------------- | ---------------- |
| 配置类                     | `@Configuration`            | refresh() 第 ⑤ 步       | 第 ⑪ 步（或被依赖时）   | 定义 `@Bean`     |
| 普通组件                   | `@Component`, `@Service`    | 第 ⑤ 步                 | 第 ⑪ 步                 | 业务 Bean        |
| `BeanFactoryPostProcessor` | 实现该接口                  | 第 ⑤ 步                 | **第 ⑤ 步中提前实例化** | 修改 Bean 定义   |
| `BeanPostProcessor`        | 实现该接口                  | 第 ⑤ 步                 | **第 ⑥ 步提前实例化**   | 增强 Bean 初始化 |
| AOP 代理类                 | `@Transactional`, `@Aspect` | —（动态生成）           | 目标 Bean 初始化时      | 运行时增强       |
| 懒加载 Bean                | `@Lazy`                     | 第 ⑤ 步                 | **首次 getBean() 时**   | 延迟初始化       |
