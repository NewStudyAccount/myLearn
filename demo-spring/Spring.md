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







### 1. **设置类加载器和表达式解析器**

```java
beanFactory.setBeanClassLoader(getClassLoader());
beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
```

- **`setBeanClassLoader`**：设置 Bean 的类加载器为当前上下文的类加载器，确保在需要动态加载类时使用正确的 ClassLoader。
- **`setBeanExpressionResolver`**：配置 Spring 表达式语言（SpEL）的解析器，允许在 Bean 配置中使用如 `#{systemProperties.myProp}` 的表达式。
- **`addPropertyEditorRegistrar`**：注册属性编辑器（PropertyEditor），用于将字符串（如配置文件中的值）转换为特定类型（如 `Resource`、`File` 等）。`ResourceEditorRegistrar` 主要支持 `Resource` 类型的自动转换。

### 2. **注册 `ApplicationContextAwareProcessor` 并忽略某些 Aware 接口的自动注入**

```java
beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
// ... 其他 ignoreDependencyInterface 调用
```

- **`ApplicationContextAwareProcessor`** 是一个 `BeanPostProcessor`，它会在 Bean 初始化后检查是否实现了某些 `*Aware` 接口（如 `ApplicationContextAware`、`EnvironmentAware` 等），如果实现了就自动注入对应的依赖（如 `ApplicationContext`、`Environment` 等）。
- **`ignoreDependencyInterface`** 告诉 Spring：这些接口的 setter 方法（如 `setApplicationContext`）**不要通过自动装配（byType）来注入**，因为它们会由 `ApplicationContextAwareProcessor` 来处理。避免重复或冲突注入。

### 3. **注册可解析的依赖类型（Resolvable Dependencies）**

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

### 4. **注册 `ApplicationListenerDetector`**

```java
beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
```

- 这个 `BeanPostProcessor` 用于检测那些实现了 `ApplicationListener` 接口的 Bean，并将它们注册到 Spring 的事件广播器中，使其能接收 `ApplicationEvent`。
- 如果 Bean 是内部 Bean（inner bean）或作用域不符合要求，会将其从监听器列表中移除。

### 5. **支持 LoadTimeWeaving（如果启用）**

```java
if (!NativeDetector.inNativeImage() && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
    beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
    beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
}
```

- 如果容器中存在名为 `loadTimeWeaver` 的 Bean（通常由 `<context:load-time-weaver/>` 配置），说明启用了 **AspectJ 的加载时织入（Load-Time Weaving）**。
- 此时注册 `LoadTimeWeaverAwareProcessor`，用于将 `LoadTimeWeaver` 注入到实现了 `LoadTimeWeaverAware` 的 Bean 中。
- 设置临时 ClassLoader 用于类型匹配（因为织入可能改变类结构）。

### 6. **注册环境相关的单例 Bean**

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





```
preInstantiateSingletons
```





## 第二步：Bean 注册 —— `BeanDefinition` 是什么？
