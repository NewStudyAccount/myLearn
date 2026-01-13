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



## 三、 Bean 的初始化与 AOP 入口

回到 `doCreateBean` 中的第三步 `initializeBean`。这里决定了你的 Bean 是原生的，还是被 AOP 代理过的。

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
Step 4 是关键：Spring AOP 是通过一个特殊的 `BeanPostProcessor`（叫做 `AnnotationAwareAspectJAutoProxyCreator`）来实现的。
在 `applyBeanPostProcessorsAfterInitialization` 这一步，Spring 会检查：“这个 Bean 是否被切面（Aspect）覆盖了？”
如果是：创建一个代理对象（Proxy）包裹原始对象，并返回代理对象。
如果否：直接返回原始对象。





### 四、 AOP 代理的执行原理 (JDK 动态代理)

如果 Spring 决定创建代理，它通常使用 JDK 动态代理（针对接口）或 CGLIB（针对类）。我们看更清晰的 JDK 动态代理 `JdkDynamicAopProxy`。

它实现了 `InvocationHandler` 接口，意味着所有方法调用都会先走 invoke 方法。

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
`ReflectiveMethodInvocation`: 这是一个典型的责任链模式。它会像剥洋葱一样，先执行 Before Advice，再往里层走，直到执行真正的 target.save()，回来时再执行 After Advice。



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





### 手写一个“Mini-Spring”是理解 Spring 源码最快的方式。

我们将实现 Spring 最核心的三个功能：

Bean 容器：管理对象的生命周期。
依赖注入 (DI)：解析 @Autowired 自动赋值。
AOP (简易版)：解析自定义注解，生成代理对象增强功能



项目结构
为了简单明了，我们将所有代码放在一个包下，不分模块。

- MyApplicationContext: 核心容器。
- @MyComponent: 标记 Bean。
- @MyAutowired: 标记注入点。
- @MyTransactional: 标记需要 AOP 增强的方法（模拟事务）。
- 业务测试代码：UserService 和 UserDao。



#### 第一步：定义核心注解

```java
import java.lang.annotation.*;

// 1. 模仿 @Component 或 @Service
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyComponent {
    String value() default ""; // Bean 的名字
}

// 2. 模仿 @Autowired
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyAutowired {
}

// 3. 模仿 @Transactional (作为 AOP 的切点)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyTransactional {
}

```

#### 第二步：实现核心容器 (MyApplicationContext)

这是整个“Mini-Spring”的大脑。

```java
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class MyApplicationContext {

    // 单例池：存放已经创建好的 Bean (Map<BeanName, BeanInstance>)
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 构造方法：传入配置类或包名（这里简化为直接传递要管理的类数组）
    public MyApplicationContext(Class<?>... componentClasses) {
        try {
            // 1. 【注册 & 实例化】：遍历所有类，创建对象放入容器
            for (Class<?> clazz : componentClasses) {
                if (clazz.isAnnotationPresent(MyComponent.class)) {
                    // 获取 Bean 的名字 (简化策略：首字母小写)
                    MyComponent annotation = clazz.getAnnotation(MyComponent.class);
                    String beanName = annotation.value();
                    if (beanName.isEmpty()) {
                        beanName = lowerFirst(clazz.getSimpleName());
                    }

                    // 【核心】实例化 Bean (相当于 new)
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    
                    // 将半成品 Bean 放入容器
                    singletonObjects.put(beanName, instance);
                }
            }

            // 2. 【依赖注入】：遍历容器中的 Bean，处理 @Autowired
            for (Object instance : singletonObjects.values()) {
                populateBean(instance);
            }
            
            // 3. 【AOP 处理】：遍历容器，检查是否有需要代理的方法
            // 注意：这里为了简化，直接替换单例池中的对象。
            // 实际 Spring 中，AOP 是在 BeanPostProcessor 中发生的，并在注入前或初始化后完成。
            // 这里我们采用"后置替换"策略来演示原理。
            Map<String, Object> proxyMap = new ConcurrentHashMap<>();
            for (Map.Entry<String, Object> entry : singletonObjects.entrySet()) {
                String beanName = entry.getKey();
                Object instance = entry.getValue();
                
                Object proxyInstance = processAop(instance);
                if (proxyInstance != instance) {
                    proxyMap.put(beanName, proxyInstance);
                }
            }
            // 将代理对象更新回容器
            singletonObjects.putAll(proxyMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取 Bean 的方法
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }

    // --- 核心逻辑 2：依赖注入 ---
    private void populateBean(Object instance) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                // 获取字段名作为 Bean ID (简化策略：假设字段名就是 Bean ID)
                String fieldName = field.getName();
                Object dependency = singletonObjects.get(fieldName);

                if (dependency != null) {
                    field.setAccessible(true); // 暴力反射
                    field.set(instance, dependency); // 赋值
                }
            }
        }
    }

    // --- 核心逻辑 3：AOP 动态代理 ---
    private Object processAop(Object bean) {
        Class<?> clazz = bean.getClass();
        
        // 检查是否有方法标记了 @MyTransactional
        boolean needProxy = false;
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MyTransactional.class)) {
                needProxy = true;
                break;
            }
        }

        if (!needProxy) {
            return bean; // 不需要代理，返回原对象
        }

        // 使用 JDK 动态代理生成代理对象
        // 这里的要求是：类必须实现接口
        if (clazz.getInterfaces().length == 0) {
            System.out.println("警告: " + clazz.getName() + " 需要 AOP 但没有实现接口，JDK 代理无法生效。");
            return bean;
        }

        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                clazz.getInterfaces(),
                (proxy, method, args) -> {
                    // 这里是代理逻辑
                    // 1. 检查当前执行的方法是否有注解
                    // 注意：method 是接口的方法，我们需要找实现类的方法上的注解
                    Method targetMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                    
                    if (targetMethod.isAnnotationPresent(MyTransactional.class)) {
                        System.out.println(">>> [AOP 开启事务] " + method.getName());
                        Object result = null;
                        try {
                            result = method.invoke(bean, args); // 执行原业务方法
                            System.out.println(">>> [AOP 提交事务]");
                        } catch (Exception e) {
                            System.out.println(">>> [AOP 回滚事务] 出现异常: " + e.getCause().getMessage());
                            throw e; 
                        }
                        return result;
                    } else {
                        // 没有注解的方法，直接执行，不增强
                        return method.invoke(bean, args);
                    }
                }
        );
    }

    // 工具方法：首字母小写
    private String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}

```



#### 第三步：定义业务代码 (DAO 和 Service)

我们需要一个接口，因为 JDK 动态代理必须基于接口。

```java
// 1. 接口
public interface IUserService {
    void createUser(String name);
}

// 2. DAO (数据访问层)
@MyComponent("userDao")
public class UserDao {
    public void save() {
        System.out.println("UserDao: 保存数据到数据库...");
    }
}

// 3. Service (业务逻辑层)
@MyComponent("userService")
public class UserServiceImpl implements IUserService {

    @MyAutowired
    private UserDao userDao; // 等待注入

    @Override
    @MyTransactional // 需要事务增强
    public void createUser(String name) {
        System.out.println("UserService: 正在处理用户业务逻辑 -> " + name);
        userDao.save(); // 调用依赖
        // 模拟异常测试回滚
        if ("error".equals(name)) {
             throw new RuntimeException("故意抛出的异常");
        }
    }
}

```



#### 第四步：测试运行

```java
public class MySpringTest {
    public static void main(String[] args) {
        // 1. 启动容器，扫描并注册指定的类
        MyApplicationContext context = new MyApplicationContext(UserServiceImpl.class, UserDao.class);

        // 2. 从容器获取 Bean
        // 注意：因为生成了代理对象，代理对象也是 IUserService 的实例
        IUserService userService = (IUserService) context.getBean("userService");

        // 3. 测试正常情况
        System.out.println("----------------- 测试正常流程 -----------------");
        userService.createUser("Turing");

        // 4. 测试 AOP 异常回滚情况
        System.out.println("\n----------------- 测试异常回滚 -----------------");
        try {
            userService.createUser("error");
        } catch (Exception e) {
            // 忽略异常打印，只看控制台日志
        }
    }
}

```

#### 总结我们实现了什么

1. IoC 实现：我们在 MyApplicationContext 构造函数中，通过反射 newInstance 创建了 UserDao 和 UserServiceImpl，并将它们放入 Map 中。
2. DI 实现：在 populateBean 方法中，我们扫描 UserServiceImpl 的字段，发现 @MyAutowired，于是从 Map 中取出 userDao 并暴力塞入 UserServiceImpl 中。
3. AOP 实现：在 processAop 中，我们发现 UserServiceImpl 的方法上有 @MyTransactional，于是利用 JDK Proxy.newProxyInstance 创建了一个代理对象替换了原本的 Bean。当我们调用 createUser 时，实际执行的是 InvocationHandler 中的逻辑（打印事务日志 + 调用原方法）。