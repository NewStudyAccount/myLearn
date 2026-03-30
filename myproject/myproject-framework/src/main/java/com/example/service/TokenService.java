package com.example.service;


import com.example.domain.MyUserDetails;

public interface TokenService {
    String createToken(MyUserDetails myUserDetails);


    public MyUserDetails getUserDetailsFromToken(String token);
}
