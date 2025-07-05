package com.example.domain.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVo {

    @JsonProperty(value = "userName")
    private String userName;

    @JsonProperty(value = "passWord")
    private String password;
}
