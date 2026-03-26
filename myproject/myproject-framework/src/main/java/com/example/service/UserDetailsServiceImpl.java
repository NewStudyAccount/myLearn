package com.example.service;


import com.example.domain.MyUserDetails;
import com.example.domain.SysUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SecurityAdminService securityAdminService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDto sysUserDto = securityAdminService.queryByUserName(username);
        if (Objects.isNull(sysUserDto)){
            throw new UsernameNotFoundException("用户不存在");
        }
        //用户的权限信息
        Long userId = sysUserDto.getUserId();
        List<String> userPermission = securityAdminService.getUserPermission(userId);

        MyUserDetails myUserDetails = new  MyUserDetails(sysUserDto,userPermission);


        return myUserDetails;
    }
}
