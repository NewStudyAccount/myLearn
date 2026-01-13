package com.example.applicationContext;

import com.example.annotation.MyAutowired;
import com.example.annotation.MyComponent;
import com.example.annotation.MyTransactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
