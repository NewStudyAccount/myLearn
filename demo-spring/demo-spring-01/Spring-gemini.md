这些代码主要位于 `spring-beans`,` spring-context`, `spring-aop` 模块中。我会对源码进行适当的精简（去掉了日志、异常处理、复杂的边界判断），只保留核心逻辑，并配合中文注释解释。

# 一、 Bean 的创建流程 (IoC 核心)

Spring 中管理 Bean 最核心的类是 `DefaultListableBeanFactory`，其父类 `AbstractAutowireCapableBeanFactory` 实现了具体的 Bean 创建逻辑。

入口方法是 `getBean`，最终会调用到 `doCreateBean`。这是 Spring 源码中最著名的方法之一。

源码位置： `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory`



```java
// 核心方法：doCreateBean
protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {

    // 1. 【实例化】(Instantiation)
    // 相当于 new Object()，通常使用反射调用构造函数。
    // BeanWrapper 是对 Bean 实例的包装，方便后续设置属性。
    BeanWrapper instanceWrapper = createBeanInstance(beanName, mbd, args);
    Object bean = instanceWrapper.getWrappedInstance();

    // ... (此处省略了解决循环依赖的三级缓存逻辑) ...

    // 2. 【属性填充】(Populate)
    // 解析 @Autowired, @Value 等注解，将依赖注入到 Bean 中。
    populateBean(beanName, mbd, instanceWrapper);

    // 3. 【初始化】(Initialization)
    // 执行 Aware 接口、@PostConstruct、InitializingBean 接口、AOP 代理等。
    Object exposedObject = initializeBean(beanName, bean, mbd);

    return exposedObject;
}

```

**源码解读：**

- Step 1 (实例化): Spring 并不是一开始就拥有完整的对象，而是先通过反射（Reflection）只是把内存申请下来，得到一个“空壳对象”。
- Step 2 (填充): populateBean 是依赖注入发生的时刻。它会遍历该对象的所有属性，在容器中找到对应的依赖并赋值。
- Step 3 (初始化): 这是 Bean 生命周期的重头戏，AOP 代理通常也是在这里生成的。



## 二、 依赖注入的实现 (DI 核心)

`populateBean` 方法内部会使用各种 `BeanPostProcessor` 来处理注入。最常用的是处理 `@Autowired` 的 `AutowiredAnnotationBeanPostProcessor`。

源码位置： `org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor`

````java
// 内部类 AutowiredFieldElement 的 inject 方法
@Override
protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
    
    // 1. 获取要做注入的字段 (例如 private UserDao userDao;)
    Field field = (Field) this.member;

    try {
        Object value;
        // 2. 【核心】在容器中寻找依赖对象
        // beanFactory.resolveDependency 会根据类型（Type）或名称（Name）去 Map 中找 Bean
        value = beanFactory.resolveDependency(new DependencyDescriptor(field, this.required), beanName);
        
        if (value != null) {
            // 3. 利用反射暴力破解 private 权限
            ReflectionUtils.makeAccessible(field);
            
            // 4. 【核心】反射赋值：相当于 field.set(bean, value)
            field.set(bean, value);
        }
    } catch (Throwable ex) {
        throw new BeanCreationException("Could not autowire field: " + field, ex);
    }
}

````

源码解读：
这就是 `@Autowired` 的本质。Spring 扫描到注解后，记录下字段信息，然后在 Bean 创建过程中，通过 `beanFactory` 找到依赖对象，最后通过 Java 原生的反射 `field.set()` 完成赋值。



三、 Bean 的初始化与 AOP 入口
回到 doCreateBean 中的第三步 initializeBean。这里决定了你的 Bean 是原生的，还是被 AOP 代理过的。

源码位置： org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory

````java
protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
    
    // 1. 执行 Aware 接口方法 (如 BeanNameAware, ApplicationContextAware)
    invokeAwareMethods(beanName, bean);

    // 2. 执行 BeanPostProcessor 的【前置】处理
    // @PostConstruct 注解就是在这里被 CommonAnnotationBeanPostProcessor 处理的
    Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

    // 3. 执行初始化方法 (InitializingBean 接口的 afterPropertiesSet 方法)
    invokeInitMethods(beanName, wrappedBean, mbd);

    // 4. 执行 BeanPostProcessor 的【后置】处理 (!!! AOP 的发生地 !!!)
    // 这里的 wrappedBean 可能被替换成 代理对象
    wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

    return wrappedBean;
}

````

源码解读：
Step 4 是关键：Spring AOP 是通过一个特殊的 BeanPostProcessor（叫做 AnnotationAwareAspectJAutoProxyCreator）来实现的。
在 applyBeanPostProcessorsAfterInitialization 这一步，Spring 会检查：“这个 Bean 是否被切面（Aspect）覆盖了？”
如果是：创建一个代理对象（Proxy）包裹原始对象，并返回代理对象。
如果否：直接返回原始对象。





四、 AOP 代理的执行原理 (JDK 动态代理)
如果 Spring 决定创建代理，它通常使用 JDK 动态代理（针对接口）或 CGLIB（针对类）。我们看更清晰的 JDK 动态代理 JdkDynamicAopProxy。

它实现了 InvocationHandler 接口，意味着所有方法调用都会先走 invoke 方法。

源码位置： org.springframework.aop.framework.JdkDynamicAopProxy

````java
final class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    // 原始的目标对象 (Target Source)
    private final TargetSource targetSource;
    // 切面配置 (Advisors)
    private final AdvisedSupport advised;

    @Override
    @Nullable
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = null;
        try {
            // 1. 获取目标对象
            target = targetSource.getTarget();

            // 2. 【核心】获取该方法对应的拦截器链 (Interceptors / Advice)
            // 比如 @Before, @After 等通知都会被转换成拦截器链
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

            // 3. 如果链为空，直接反射调用目标方法
            if (chain.isEmpty()) {
                return method.invoke(target, args);
            } 
            // 4. 如果链不为空，创建 MethodInvocation 并递归调用 (责任链模式)
            else {
                MethodInvocation invocation = new ReflectiveMethodInvocation(
                        proxy, target, method, args, targetClass, chain);
                // proceed() 会依次执行 @Before -> 目标方法 -> @After
                return invocation.proceed();
            }
        } finally {
             // ... 清理工作
        }
    }
}

````

源码解读：
当你拿到一个 Service 并调用 userService.save() 时，你实际上是在调用 JdkDynamicAopProxy.invoke()。
拦截器链 (Chain): Spring 会找出所有匹配该方法的增强逻辑（比如事务开启、日志记录）。
ReflectiveMethodInvocation: 这是一个典型的责任链模式。它会像剥洋葱一样，先执行 Before Advice，再往里层走，直到执行真正的 target.save()，回来时再执行 After Advice。



总结串联
启动： Spring 容器启动，扫描类。
创建 (IoC)： 调用 doCreateBean。
createBeanInstance: new 出来一个空对象。
populateBean: 利用反射 field.set 注入 @Autowired 的属性。
加工 (AOP)： 调用 initializeBean。
在 applyBeanPostProcessorsAfterInitialization 阶段，如果发现该类需要事务或日志，就用 JdkDynamicAopProxy 把它包装起来。
运行： 容器中存的是 Proxy 对象。
调用： 用户调用方法 -> JdkDynamicAopProxy.invoke -> 执行切面逻辑 -> 执行目标方法。
这就是 Spring 魔法背后的真实代码逻辑。