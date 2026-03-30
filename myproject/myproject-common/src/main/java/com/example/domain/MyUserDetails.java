package com.example.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //放置在类上，表示在反序列化 JSON 数据时忽略未知的属性（即 JSON 中存在但在 Java 类中不存在的字段），避免因字段不匹配而抛出异常。
public class MyUserDetails implements UserDetails {


    private SysUserDto sysUserDto;

    private List<String> permissionList;

    @JsonIgnore //放置在方法或字段上，表示在序列化和反序列化过程中忽略该字段或方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return sysUserDto.getUserPwd();
    }

    @Override
    public String getUsername() {
        return sysUserDto.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
