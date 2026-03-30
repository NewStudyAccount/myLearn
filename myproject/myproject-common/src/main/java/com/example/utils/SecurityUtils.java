package com.example.utils;

import com.example.domain.MyUserDetails;
import com.example.domain.SysUserDto;
import com.example.execption.BizException;
import com.example.execption.ResponseCodeEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public class SecurityUtils {



    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 获取用户
     **/
    public static MyUserDetails getLoginUser()
    {
        try
        {
            return (MyUserDetails) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }
    }

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new BizException(ResponseCodeEnum.USER_NAME_NOT_FOUND);
        }
    }

    public static Long getLoginUserId()
    {
        try
        {
            SysUserDto sysUserDto = getLoginUser().getSysUserDto();
            return sysUserDto.getUserId();
        }
        catch (Exception e)
        {
            throw new BizException(ResponseCodeEnum.USER_ID_NOT_FOUND);
        }
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }


}
