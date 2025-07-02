package com.example.service;


import com.example.domain.SysUserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SecurityAdminService {


    public SysUserDto queryByUserName(String userName);

    public List<String> getUserPermission(Long userId);


}
