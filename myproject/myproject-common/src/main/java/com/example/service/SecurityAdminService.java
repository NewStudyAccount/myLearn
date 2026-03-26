package com.example.service;


import com.example.domain.SysUserDto;
import org.springframework.stereotype.Service;

@Service
public interface SecurityAdminService {


    public SysUserDto queryByUserName(String userName);



}
