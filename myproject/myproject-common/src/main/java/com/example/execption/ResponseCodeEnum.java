package com.example.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中..."),

    // ----------- 通用异常状态码 -----------
    PARAM_NOT_VALID("10001", "参数错误"),
    // ----------- 业务异常状态码 -----------
    PRODUCT_NOT_FOUND("20000", "该产品不存在（测试使用）"),
    ;

    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}
