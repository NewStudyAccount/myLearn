package com.example.service;


import com.example.domain.Response;
import com.example.domain.vo.LoginUserVo;

public interface LoginService {

    public Response login(LoginUserVo loginUserVo);
}
