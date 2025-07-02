package com.example.filter;



import com.example.domain.MyUserDetails;
import com.example.redis.RedisCache;
import com.example.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TokenService tokenService;


    // 定义白名单路径
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/project/admin/login",
            "/project/admin/register"
    );



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();

        // 白名单路径直接放行
        if (WHITE_LIST.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }


        //获取请求头中的token
        String token = request.getHeader("access_token");
        MyUserDetails userDetailsFromToken = new MyUserDetails();
        //判断token是否为空，为空则抛出异常
        if (!StringUtils.isEmpty(token)) {

            //取得token中的用户表示，注意解析的异常处理
            try {
                 userDetailsFromToken = tokenService.getUserDetailsFromToken(token);
            } catch (Exception e) {
                throw new BadCredentialsException("token错误或token过期");
            }

            if (Objects.isNull(userDetailsFromToken)){
                throw new BadCredentialsException("redis 读取null ==》token错误或token过期");
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetailsFromToken, null, userDetailsFromToken.getAuthorities());
            //将用户信息存入安全上下文
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        //过滤放行
        filterChain.doFilter(request, response);
    }
}
