package com.example.service;


import com.example.domain.Response;
import com.example.domain.vo.LoginUserVo;

public interface AuthService {

    public Response login(LoginUserVo loginUserVo);
}
