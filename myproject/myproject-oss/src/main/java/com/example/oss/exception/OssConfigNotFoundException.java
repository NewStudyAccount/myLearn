package com.example.oss.exception;

/**
 * OSS配置未找到异常
 */
public class OssConfigNotFoundException extends RuntimeException {

    public OssConfigNotFoundException(String message) {
        super(message);
    }

    public OssConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}