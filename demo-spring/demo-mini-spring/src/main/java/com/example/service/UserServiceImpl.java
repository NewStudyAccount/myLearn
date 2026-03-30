package com.example.service;

import com.example.annotation.MyAutowired;
import com.example.annotation.MyComponent;
import com.example.annotation.MyTransactional;

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