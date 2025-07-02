package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口都允许跨域
                .allowedOriginPatterns("*") // 允许所有来源，也可指定具体域名如 "http://localhost:8080"
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // 支持的方法
                .allowedHeaders("*") // 允许的请求头
                .exposedHeaders() // 可选：暴露给前端的响应头
                .allowCredentials(true) // 是否允许发送 Cookie
                .maxAge(3600); // 预检请求的缓存时间（秒）
    }
}