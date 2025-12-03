🧩 一、整体流程回顾（结合源码阶段）
Spring 启动容器 → 扫描 Bean → 注册定义 → 实例化 → 依赖注入 → 初始化

每个阶段对应一组核心类：

阶段
核心类/接口
关键方法
启动容器
AnnotationConfigApplicationContext
refresh()
扫描组件
ClassPathBeanDefinitionScanner
doScan()
注册 Bean 定义
BeanDefinitionRegistry
registerBeanDefinition()
实例化 & 注入
AbstractAutowireCapableBeanFactory
createBean() → doCreateBean()
依赖注入处理
AutowiredAnnotationBeanPostProcessor
postProcessProperties()
初始化回调
InitializingBean, @PostConstruct
invokeInitMethods()

🔍 二、关键源码位置详解
1. 容器启动入口：refresh()
   类：AbstractApplicationContext
   方法：public void refresh()
   作用：启动整个 Spring 容器生命周期，是 IoC 初始化的总控流程。
   java
   12345678910111213141516171819
   ✅ finishBeanFactoryInitialization() → 触发所有单例 Bean 的创建和注入。

2. 组件扫描与注册：ClassPathBeanDefinitionScanner
   类：org.springframework.context.annotation.ClassPathBeanDefinitionScanner
   方法：protected Set<BeanDefinitionHolder> doScan(String... basePackages)
   作用：扫描带 @Component 等注解的类，转化为 BeanDefinition 并注册。
   java
   1234567
3. Bean 实例化与依赖注入核心：AbstractAutowireCapableBeanFactory
   类：org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
   方法：
   createBean(String, RootBeanDefinition, Object[])
   doCreateBean(...) → 真正创建实例
   populateBean(...) → 依赖注入发生处
   initializeBean(...) → 初始化（调用 @PostConstruct 等）
   java
   12345678
4. @Autowired 注解的处理：AutowiredAnnotationBeanPostProcessor
   类：org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
   关键方法：public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
   作用：在 populateBean() 阶段被调用，解析 @Autowired、@Value 字段/方法，并注入依赖。
   内部逻辑：

通过反射获取 Bean 类的字段和方法；
查找带 @Autowired 的元素；
调用 BeanFactory.resolveDependency() 从容器中找匹配的 Bean；
通过反射设值（field.set(bean, dependency)）。
✅ 这就是 “反射 + 注解” 实现自动注入 的核心！

5. 后置处理器（BeanPostProcessor）机制
   Spring 在 Bean 创建前后插入扩展点：

InstantiationAwareBeanPostProcessor：可在实例化前干预（如 AOP 代理）
BeanPostProcessor：在初始化前后处理（如 @PostConstruct 由 InitDestroyAnnotationBeanPostProcessor 处理）
这些处理器在 registerBeanPostProcessors() 阶段被注册，在 initializeBean() 中调用：

java
12345
🧠 三、总结：你提到的四项如何对应源码
技术
源码体现
反射
createBeanInstance()（构造器/工厂方法调用）、field.set()、method.invoke()
注解
@Component, @Autowired 等由 ClassPathBeanDefinitionScanner 和 AutowiredAnnotationBeanPostProcessor 解析
配置
@Configuration, @Bean, XML、application.properties → 由 ConfigurationClassPostProcessor 等处理
后置处理器
BeanPostProcessor 体系（如 AutowiredAnnotationBeanPostProcessor、CommonAnnotationBeanPostProcessor）
🔎 建议阅读顺序（从宏观到微观）
AbstractApplicationContext.refresh()
finishBeanFactoryInitialization()
DefaultListableBeanFactory.preInstantiateSingletons()
AbstractAutowireCapableBeanFactory.doCreateBean()
populateBean() → 触发 AutowiredAnnotationBeanPostProcessor.postProcessProperties()