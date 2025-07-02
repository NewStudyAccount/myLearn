package com.example.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T>{

    private String code;

    private String msg;

    private T data;


    public static<T> Response<T> success(String msg, T data) {
        return new Response("200", msg, data);
    }

    public static<T> Response<T> authSuccess(T data) {
        return new Response<T>("200", "authentication success", data);
    }


    public static<T> Response<T> authFailure(T data) {
        return new Response<T>("401", "认证失败401", data);
    }

    public static<T> Response<T> accessDenied(T data) {
        return new Response<T>("403", "授权失败403", data);
    }

    public static<T> Response<T> logoutSuccess(T data) {
        return new Response<T>("200", "logoutSuccess success", data);
    }



    public static<T> Response<T> fail(String errCode,String msg) {
        Response<T> response = new Response<>();
        response.setCode(errCode);
        response.setMsg(msg);
        return response;
    }

    public static<T> Response<T> fail(String msg) {
        Response<T> response = new Response<>();
        response.setCode("8888");
        response.setMsg(msg);
        return response;
    }


    public static<T> Response<T> success(T data) {
        return new Response<T>("200", "success", data);
    }



}
