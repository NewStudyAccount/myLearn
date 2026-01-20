package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @GetMapping("/")
    public String home() {
        return "<h1>首页 (公开)</h1> <a href='/secured'>点击这里登录进入受保护区域</a>";
    }

    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal OidcUser principal) {
        return String.format("""
            <h1>登录成功 (受保护)</h1>
            <h3>用户名: %s</h3>
            <h3>Token 属性: %s</h3>
            """, principal.getName(), principal.getAttributes());
    }
}
