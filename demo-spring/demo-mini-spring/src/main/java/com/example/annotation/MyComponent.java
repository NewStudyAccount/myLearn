package com.example.annotation;

import java.lang.annotation.*;

// 1. 模仿 @Component 或 @Service
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyComponent {
    String value() default ""; // Bean 的名字
}


