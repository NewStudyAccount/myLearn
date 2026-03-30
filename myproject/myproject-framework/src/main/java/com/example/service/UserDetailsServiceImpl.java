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


        //todo 后续用来扩展
//        if (StringUtils.isNull(user))
//        {
//            log.info("登录用户：{} 不存在.", username);
//            throw new ServiceException(MessageUtils.message("user.not.exists"));
//        }
//        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
//        {
//            log.info("登录用户：{} 已被删除.", username);
//            throw new ServiceException(MessageUtils.message("user.password.delete"));
//        }
//        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
//        {
//            log.info("登录用户：{} 已被停用.", username);
//            throw new ServiceException(MessageUtils.message("user.blocked"));
//        }


        //用户的权限信息
        Long userId = sysUserDto.getUserId();
        List<String> userPermission = securityAdminService.getUserPermission(userId);


        return new  MyUserDetails(sysUserDto,userPermission);
    }
}
