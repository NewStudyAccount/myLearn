package com.example.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultInterface<T> {


    void handle(ResultSet rs) throws SQLException;

    T handleObject(ResultSet rs,Class<T> clazz) throws SQLException;


}
