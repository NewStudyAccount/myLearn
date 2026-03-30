package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll() // 首页不需要登录
                .anyRequest().authenticated()     // 其他页面需要登录
            )
            // 启用 OAuth2 登录，这就足够了，Spring 会读取 application.yml
            .oauth2Login(Customizer.withDefaults());
            
        return http.build();
    }
}
