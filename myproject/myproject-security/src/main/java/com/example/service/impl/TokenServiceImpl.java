package com.example.service.impl;


import com.example.constant.TokenConstant;
import com.example.domain.MyUserDetails;
import com.example.domain.SysUserDto;
import com.example.redis.RedisCache;
import com.example.service.TokenService;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenServiceImpl implements TokenService {


    @Autowired
    private RedisCache redisCache;



    @Override
    public String createToken(MyUserDetails myUserDetails) {

        SysUserDto sysUserDto = myUserDetails.getSysUserDto();
        Long userId = sysUserDto.getUserId();

        //生成token  token中存储的是map   用户的user_id,  后续取出token中存储的user_id  组装rediskey 获取用户信息
        Map<String, Object> claims = new HashMap<>();

        claims.put(TokenConstant.CLAIMS_KEY,userId);
        String token = null;
        try {
            token = JwtUtils.createToken(claims);
        } catch (Exception e) {
            throw new RuntimeException("jwttoken生成报错"+e.getMessage());
        }
        //token生成成功将用户信息 存入到redis中
        //redis redis中存储的是token信息   redis key  login_user_key+ user_id   value  是 MyUserDetails信息

        String redisKey = createRedisKey(userId);
        redisCache.setCacheObject(redisKey,myUserDetails,TokenConstant.REDIS_LOGIN_KEY_EXPIRE_TIME, TimeUnit.MINUTES);

        return token;
    }

    @Override
    public MyUserDetails getUserDetailsFromToken(String token) {
        Claims claims = JwtUtils.parseJwt(token);
        Long loginUserId = Long.valueOf(claims.get(TokenConstant.CLAIMS_KEY).toString());

        String redisKey = createRedisKey(loginUserId);
        MyUserDetails myUserDetails = (MyUserDetails) redisCache.getCacheObject(redisKey);

        return myUserDetails;
    }


    public String createRedisKey(Long userId){
        return TokenConstant.REDIS_LOGIN_KEY+":"+userId;
    }

}
