package com.example.oss.exception;

/**
 * OSS客户端创建异常
 */
public class OssClientCreateException extends RuntimeException {

    public OssClientCreateException(String message) {
        super(message);
    }

    public OssClientCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}