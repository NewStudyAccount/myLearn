package com.example.security.handler;



import com.example.domain.Response;
import com.example.utils.ResponseModelUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

/**
 * 认证异常处理
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        // 检查请求是否有对应的处理器（即是否存在对应的 Controller 映射）
        boolean hasHandler = checkHasHandler(request);
        
        if (!hasHandler) {
            // 如果没有对应的处理器，返回 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Response responseModel = Response.fail("404", "请求的资源不存在");
            ResponseModelUtils.write(response, responseModel);
        } else {
            // 如果有对应的处理器，但认证失败，返回 401
            Response responseModel = Response.authFailure(e.getMessage());
            ResponseModelUtils.write(response, responseModel);
        }
    }

    /**
     * 检查请求是否有对应的处理器
     */
    private boolean checkHasHandler(HttpServletRequest request) {
//        try {
//            // 使用 RequestMappingHandlerMapping 的 getHandler 方法检查请求是否有匹配的处理器
//            HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
//            return handler != null && handler.getHandler() instanceof HandlerMethod;
//        } catch (Exception ex) {
//            // 如果检查失败，默认认为有处理器（保持原有行为）
//            return true;
//        }
        try {
            HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);

            if (handler == null) {
                return false;
            }

            Object handlerObj = handler.getHandler();

            if (handlerObj instanceof HandlerMethod) {
                return true;
            }

            String handlerClassName = handlerObj.getClass().getName();
            if (handlerClassName.contains("ResourceHttpRequestHandler")) {
                return false;
            }

            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}