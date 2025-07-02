package com.example.config;


import com.example.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //自定义的认证管理器对象
    @Bean
    public AuthenticationManager authenticationManager() {
        //匹配合适的AuthenticationProvider(DaoAuthenticationProvider)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //配置基于数据库认证的认证UserDetailsService对象
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        //创建并返回认证管理器对象(实现类ProviderManager)
        return new ProviderManager(provider);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth

                .requestMatchers(
//                        "/**",
                        "/webjars/**",
                        "/doc.html/**","/v3/api-docs/**","/swagger-ui.html/**",
                        "/project/admin/login","/project/admin/register")
                        .permitAll()  //自定义的登录接口不需要验证
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // 先配置过滤器
                .exceptionHandling(execption->execption.authenticationEntryPoint(authenticationEntryPoint)  // 后配置异常处理
                        .accessDeniedHandler(accessDeniedHandler))
                .cors(AbstractHttpConfigurer::disable);

        return http.build();
    }


}
