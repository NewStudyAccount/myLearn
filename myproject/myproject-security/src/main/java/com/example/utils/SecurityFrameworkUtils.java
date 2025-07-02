package com.example.utils;

import com.example.domain.MyUserDetails;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * 安全服务工具类
 *
 * @author 芋道源码
 */
public class SecurityFrameworkUtils {


    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    @Nullable
    public static MyUserDetails getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) authentication.getPrincipal() : null;
    }

    /**
     * 获得当前用户的编号，从上下文中
     *
     * @return 用户编号
     */
    @Nullable
    public static Long getLoginUserId() {
        MyUserDetails loginUser = getLoginUser();
        return loginUser != null ? loginUser.getSysUserDto().getUserId() : null;
    }



    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request 请求
     */
//    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
//        // 创建 Authentication，并设置到上下文
//        Authentication authentication = buildAuthentication(loginUser, request);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // 额外设置到 request 中，用于 ApiAccessLogFilter 可以获取到用户编号；
//        // 原因是，Spring Security 的 Filter 在 ApiAccessLogFilter 后面，在它记录访问日志时，线上上下文已经没有用户编号等信息
//        WebFrameworkUtils.setLoginUserId(request, loginUser.getId());
//        WebFrameworkUtils.setLoginUserType(request, loginUser.getUserType());
//    }

//    private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
//        // 创建 UsernamePasswordAuthenticationToken 对象
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                loginUser, null, Collections.emptyList());
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        return authenticationToken;
//    }

}
