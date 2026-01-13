package com.example;

import com.example.applicationContext.MyApplicationContext;
import com.example.service.IUserService;
import com.example.service.UserDao;
import com.example.service.UserServiceImpl;

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
