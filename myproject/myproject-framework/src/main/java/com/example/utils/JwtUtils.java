package com.example.utils;

import com.example.constant.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {


    //创建token
    public static String createToken(Map<String, Object> claims) {
        //过期时间
        String token = Jwts.builder()
                //头部
                .setHeaderParam("typ", "JWT") //令牌的类型为JWT令牌
                .setHeaderParam("alg", "HS256")//签名的加密算法为 HS256
                //载荷
                .setIssuer("admin")//签发人
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TokenConstant.EXPIRE_TIME))//设置jwt token过期时间
                //设置载荷信息,存储的rediskey的值。
                .setClaims(claims)
                //签名
                .signWith(SignatureAlgorithm.HS256, TokenConstant.SECRET)
                .compact();
        return token;
    }




    //解析token, 解析的时候需要增加异常处理
    public static Claims parseJwt(String token) {

        return Jwts.parser()
                .setSigningKey(TokenConstant.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


}
