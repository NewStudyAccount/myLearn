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



```
preInstantiateSingletons
```





## 第二步：Bean 注册 —— `BeanDefinition` 是什么？
