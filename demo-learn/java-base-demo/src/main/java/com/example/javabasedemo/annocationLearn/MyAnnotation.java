package com.example.javabasedemo.annocationLearn;

public @interface MyAnnotation {
    String value();           // 必须指定值
    int count() default 1;    // 可设置默认值
    String[] tags() default {}; // 数组类型
}