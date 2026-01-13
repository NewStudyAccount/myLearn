package com.example.service;

import com.example.annotation.MyComponent;

// 2. DAO (数据访问层)
@MyComponent("userDao")
public class UserDao {
    public void save() {
        System.out.println("UserDao: 保存数据到数据库...");
    }
}