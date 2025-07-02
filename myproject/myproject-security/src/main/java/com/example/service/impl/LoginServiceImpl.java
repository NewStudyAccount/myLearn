package com.example.service.impl;


import com.example.domain.MyUserDetails;
import com.example.domain.Response;
import com.example.domain.vo.LoginUserVo;
import com.example.service.LoginService;
import com.example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Override
    public Response login(LoginUserVo loginUserVo) {

        String userName = loginUserVo.getUserName();
        String password = loginUserVo.getPassword();

        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(userName, password);

        Authentication authenticate = authenticationManager.authenticate(unauthenticated);
        if (authenticate.isAuthenticated()){
            //认证成功返回结果（JSON），应注意authentication对象放入安全上下文供后续过滤器使用 （重要）
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            //认证成功,生成token返回
            MyUserDetails myUserDetails = (MyUserDetails) authenticate.getPrincipal();
            String token = tokenService.createToken(myUserDetails);
            return Response.success("登录成功",token);

        }else {
            //认证失败
            return Response.authFailure("登录失败");
        }


    }
}
