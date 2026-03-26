package com.example.security.filter;


import com.example.domain.MyUserDetails;
import com.example.redis.RedisCache;
import com.example.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        //获取请求头中的 token
        String token = request.getHeader("Authorization");
        // 没有 token 或格式不正确，直接放行
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试解析 token
        try {
            token = token.substring(7); // 去除 "Bearer " 前缀
            MyUserDetails userDetailsFromToken = tokenService.getUserDetailsFromToken(token);

            // Redis 中存在则设置认证信息
            if (Objects.nonNull(userDetailsFromToken)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetailsFromToken, null, userDetailsFromToken.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // Redis 中不存在也不抛异常，让后续的 Spring Security 处理
        } catch (Exception e) {
            // Token 解析失败，不抛异常，继续放行
            logger.debug("Token 解析失败：" + e.getMessage());
        }
        //过滤放行
        filterChain.doFilter(request, response);
    }
}
