package com.example.buildModel.domain;


import lombok.Data;

@Data
public class Food {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
