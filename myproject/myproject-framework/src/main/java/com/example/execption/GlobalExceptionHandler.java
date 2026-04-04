package com.example.execption;

import com.example.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author: 犬小哈
 * @url: www.quanxiaoha.com
 * @date: 2023-08-15 10:14
 * @description: 全局异常处理
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获自定义业务异常
     * @return
     */
    @ExceptionHandler({ BizException.class })
    @ResponseBody
    public Response<Object> handleBizException(BizException e) {
        return Response.fail(e.getErrorCode(),e.getErrorMessage());
    }


    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    @ResponseBody
    public Response<Object> handleBizException(SQLIntegrityConstraintViolationException e) {
        return Response.fail(String.valueOf(e.getErrorCode()),e.getMessage());
    }


    /**
     * 捕获参数校验异常
     * @return
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseBody
    public Response<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 参数错误异常码
        log.error(e.getMessage(), e);

        String errorCode = ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode();

        // 错误信息
        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        return Response.fail(errorCode, message);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Response handleRuntimeException(RuntimeException e) {

        return Response.fail("系统异常：" + e.getMessage());
    }
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public Response handleRuntimeException(SQLException e) {

        return Response.fail("数据库异常：" + e.getMessage());
    }


}

