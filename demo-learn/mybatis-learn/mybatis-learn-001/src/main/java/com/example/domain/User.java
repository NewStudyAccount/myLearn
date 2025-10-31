package com.example.domain;


import lombok.Data;

import java.util.StringJoiner;

@Data
public class User {

    private int id;

    private String name;

    private String pwd;

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("pwd=" + pwd)
                .toString();
    }

}
