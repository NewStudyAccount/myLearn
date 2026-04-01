package com.example.config;


import com.example.security.filter.JwtAuthenticationFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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


    //跨域配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }



    private static final String[] WHITE_LIST = {

            // Swagger 和 API 文档 - 精确路径 + 通配符
            "/v3/api-docs",
            "/v3/api-docs/default",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/doc.html",
            "/doc.html/**",
            //代码自动生成
            "/generator/**",
            "project/generator/**",

            // 登录注册接口
            "/project/auth/login",

            "/project/admin/register",

            // 网站图标
            "/favicon.ico"

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // 把所有公开路径集中写在一起，放在最前面，匹配更宽松
                        .requestMatchers(WHITE_LIST).permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        //认证失败401
                        .authenticationEntryPoint(authenticationEntryPoint)
                        //授权失败403
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }


}
